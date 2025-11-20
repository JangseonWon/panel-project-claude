package com.greencross.lims.service.issue

import com.gcgenome.lims.dto.Issue
import com.greencross.lims.dto.Query_
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
open class IssueHandler(
    private val dao: RequestDao,
    private val mapper: RequestToIssue
) {
    @Transactional(readOnly=true)
    open fun search(query: Query_): Page<Issue> {
        return dao.search(query).map(mapper::map)
    }
}