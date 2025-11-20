package com.greencross.lims.service.snv

import com.greencross.lims.dto.QueryServerside
import com.greencross.lims.entity.SnvConsensualClass
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
open class SnvHandler(
    private val dao: SnvDao,
    private val consensualClassDao: SnvConsensualClassDao
) {
    open fun snvs(query: QueryServerside?): Mono<Page<java.util.LinkedHashMap<Any, Any>>> {
        return Mono.just(dao.search(query)
            .map { h -> h.content as LinkedHashMap<Any, Any> }
            .map { content->
                val chrom = content["chrom"].toString().lowercase().replace("chr", "").padStart(2, '0')
                val pos = content["pos"].toString().padStart(9, '0')
                val ref = content["ref"]
                val alt = content["alt"]
                val id = "hg19:${chrom}:${pos}:${ref}:${alt}"
                val consensual = consensualClassDao.findLastBySnv(id)?.classification()
                content.apply {
                    content["id"] = id
                    if(consensual!=null) content["d.class"] = consensual
                }
            })
    }
}