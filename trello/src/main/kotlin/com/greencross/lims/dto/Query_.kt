package com.greencross.lims.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Query_(
    val page: Int = 0,
    val limit: Int = 0,
    @JsonProperty("sort_by")
    val sortBy: String? = null,
    val asc: Boolean? = false,
    val filters: MutableList<Filter>?
) {
    companion object {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Filter(
            val key: String,
            val value: String? = null
        )
    }
}