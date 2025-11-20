package com.greencross.lims.service.issue

import com.gcgenome.lims.dto.Issue
import com.greencross.lims.entity.readonly.*
import org.springframework.stereotype.Component

@Component
class RequestToIssue {
    fun map(request: Request): Issue {
        var state:String? = null;
        if(request is RequestNotProcessed) ;
        else if(request is RequestAnalyzed) state = "BI 분석 완료";
        else if(request is RequestInterpreted) state = "판독 완료";
        else if(request is RequestComplete) state = "완료";
        return Issue().sample(request.pk!!.sample!!).service(request.pk!!.service).state(state)
    }
}