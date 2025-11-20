package com.greencross.lims.service.issue

import com.greencross.lims.dao.AbstractJpaDAO
import com.greencross.lims.dao.GroupableJpa
import com.greencross.lims.dto.Query_
import com.greencross.lims.entity.readonly.Request
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.stream.Stream
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


@Repository
open class RequestDao : AbstractJpaDAO<Request>(), GroupableJpa<Request> {
    override fun map(cb: CriteriaBuilder, c: Root<Request>, filter: Query_.Companion.Filter): Predicate? {
        return map(cb, c, filter.key, filter.value)
    }
    private fun map(cb: CriteriaBuilder, c: Root<Request>, key: String, value: String?): Predicate? {
        return if (key.trim { it <= ' ' }.isEmpty()) {
            val predicates = Stream.of(
                map(cb, c, "sample", value),
                map(cb, c, "patient", value),
                map(cb, c, "service code", value),
                map(cb, c, "panel", value)
            ).filter(Objects::nonNull)
                .toArray<Predicate>(::arrayOfNulls)
            cb.or(*predicates)
        } else if("from".contentEquals(key, ignoreCase = true)) if(value!=null) {
            val ld: LocalDate = Instant.ofEpochMilli(value.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
            cb.greaterThanOrEqualTo(parse(c, "dateRequest"), ld)
        } else null
        else if("to".contentEquals(key, ignoreCase = true)) if(value!=null) {
            val ld: LocalDate = Instant.ofEpochMilli(value.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
            cb.lessThan(parse(c, "dateRequest"), ld.plusDays(1))
        } else null
       // else if("patient".contentEquals(key, ignoreCase = true)) if(value!=null) request.patientName.like("%$value%") else null
       // else if("service code".contentEquals(key, ignoreCase = true)) if(value!=null) request.service.eq("$value") else null)
        else if ("sample".equals(key, ignoreCase = true)) {
            val tmp = if (value!=null && value.contains("-")) value.replace("-", "") else value
            if (tmp!=null && tmp.matches("\\d+".toRegex())) cb.equal(parse<Any, Request>(c, "sample"), tmp.toLong()) else null
        } else if ("registered".contentEquals(key, ignoreCase = true))if(value!=null) cb.equal(parse<Any, Request>(c, "registered"), value.toBoolean()) else null
        else if ("canceled".contentEquals(key, ignoreCase = true)) if(value!=null) cb.equal(parse<Any, Request>(c, "canceled"), value.toBoolean()) else null
        else if ("deleted".contentEquals(key, ignoreCase = true)) if(value!=null) cb.equal(parse<Any, Request>(c, "deleted"), value.toBoolean()) else null
        else cb.equal(c.get<Any>(key), value)
    }
}