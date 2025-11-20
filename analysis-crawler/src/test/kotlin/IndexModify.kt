import kotlinx.coroutines.*
import org.apache.http.HttpHost
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.core.CountRequest
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.client.indices.GetIndexResponse
import org.elasticsearch.client.indices.GetMappingsRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.index.reindex.ReindexRequest
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class IndexModify {

    private val client = RestHighLevelClient(
        RestClient.builder(
            HttpHost("pisces", 9200, "http"),
        ).setRequestConfigCallback { conf ->
            conf.setSocketTimeout(1000 * 60 * 60 * 5)
        },
    )

    private fun getIndexTypes(prefix: String): List<Pair<String, List<Pair<String, LocalDate>>>> {
        val request = GetIndexRequest("$prefix*")
        val response: GetIndexResponse = client.indices().get(request, RequestOptions.DEFAULT)
        val indices = response.indices.map { indexName ->
            val settings = response.settings[indexName]
            val creationEpochMillis = settings?.get("index.creation_date")?.toLong()

            val creationDateTime = creationEpochMillis!!.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            indexName to creationDateTime
        }
        val compressedNames = indices.filter { (name, _) ->
            name.matches(Regex("$prefix[a-z0-9\\-]+\\d{3}")) &&
                !name.startsWith("analysis-snv-22dm")
        }.map { it.first.dropLast(3) }.toSet()

        return compressedNames.map {
            it to indices.filter { (name, _) -> name.matches(Regex("$it\\d{3}")) }
        }
    }

    private fun reindexToOne(reindexName: String, indicesToReindex: List<Pair<String, LocalDate>>) {
        // Check if reindexName already exists
        val exists = client.indices().exists(GetIndexRequest(reindexName), RequestOptions.DEFAULT)

        val request = CountRequest("$reindexName*")
        val count = client.count(request, RequestOptions.DEFAULT).count
        println("Count : $count")

        if (!exists) {
            // Sort the indices by their creation dates in descending order (latest first)
            val allMappings = mutableMapOf<String, Any>()
            val settingsBuilder = Settings.builder()
            indicesToReindex.sortedWith(compareBy({ it.second }, { it.first }))
                .last()
                .also { (indexName, _) ->
                    val getSettingsRequest = GetSettingsRequest().indices(indexName)
                    val getSettingsResponse = client.indices().getSettings(getSettingsRequest, RequestOptions.DEFAULT)

                    val indexSettings = getSettingsResponse.indexToSettings[indexName]
                    indexSettings.keySet().forEach { settingName ->
                        if (!(
                                settingName.startsWith("index.creation_date") ||
                                    settingName.startsWith("index.uuid") ||
                                    settingName.startsWith("index.version") ||
                                    settingName.startsWith("index.provided_name") ||
                                    settingName.startsWith("index.index") ||
                                    settingName.startsWith("index.sort.order") ||
                                    settingName.startsWith("index.sort.field")
                                )
                        ) {
                            val setting = indexSettings.get(settingName)
                            settingsBuilder.put(settingName, setting.replace("[", "").replace("]", ""))
                        }
                    }
                }

            indicesToReindex.sortedWith(compareBy({ it.second }, { it.first }))
                .last()
                .also { (indexName, _) ->
                    val getMappingsRequest = GetMappingsRequest().indices(indexName)
                    val getMappingsResponse = client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT)
                    val indexMapping = getMappingsResponse.mappings()[indexName]?.sourceAsMap ?: emptyMap()

                    allMappings.putAll(indexMapping)
                }
            settingsBuilder.put("index.number_of_shards", 1)
            settingsBuilder.put("index.number_of_replicas", 1)
            val createIndexRequest = CreateIndexRequest(reindexName)
                .mapping(allMappings)
                .settings(
                    settingsBuilder.build(),
                )

            client.indices().create(createIndexRequest, RequestOptions.DEFAULT)
        }

        indicesToReindex.forEach { (indexName, _) ->
            println("Reindexing $indexName")
            // Reindex the documents from each index to reindexName
            val prev = client.count(CountRequest(reindexName), RequestOptions.DEFAULT).count
            println("Aggregate Index count : $prev")
            val target = client.count(CountRequest(indexName), RequestOptions.DEFAULT).count
            println("Target Index count : $target")
            val reindexRequest = ReindexRequest()
                .setSourceIndices(indexName)
                .setDestIndex(reindexName)

            client.reindex(reindexRequest, RequestOptions.DEFAULT)
            Thread.sleep(10 * 1000)
            val curr = client.count(CountRequest(reindexName), RequestOptions.DEFAULT).count
            println("New Aggregate Index count : $curr")
            if (curr != prev + target) {
                println("Aggregate Index should be : ${prev + target}!")
                throw Exception("Reindexing failure")
            }
            // Delete the original index
            val deleteIndexRequest = DeleteIndexRequest(indexName)
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT)

            val aliasRequest = IndicesAliasesRequest()
            val aliasAction = IndicesAliasesRequest.AliasActions.add()
                .index(reindexName)
                .alias(indexName)
            aliasRequest.addAliasAction(aliasAction)
            client.indices().updateAliases(aliasRequest, RequestOptions.DEFAULT)
        }
        val newCount = client.count(request, RequestOptions.DEFAULT).count
        println("prev count: $count")
        println("new count: $newCount")
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    @Test
    fun start() {
        runBlocking {
            withContext(newFixedThreadPoolContext(5, "MyDispatcher")){
                supervisorScope {
                    getIndexTypes("analysis-snv-22")
                        .forEach {
                            launch {
                                reindexToOne(it.first, it.second)
                            }
                        }
                }
            }
        }
    }
}
