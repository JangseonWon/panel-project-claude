package com.greencross.lims.entity.readonly

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
abstract class Request {
    @EmbeddedId
    open var pk: RequestPK? = null
    @Column(name = "sample", insertable = false, updatable = false)
    private val sample: Long? = null
    @Column(name = "service",  insertable = false, updatable = false)
    private val service: String? = null
    @Column(name = "date_request")
    private val dateRequest: LocalDate? = null
    @Column(name = "date_start")
    private val dateStart: LocalDate? = null
    @Column(name = "date_due")
    private val dateDue: LocalDate? = null
    @Column(name = "date_sampling")
    private val dateSampling: LocalDate? = null
    @Column(name = "tat")
    private val tat: Int? = null
    @Column(name = "sync_time")
    private val syncTime = LocalDateTime.now()
    @Column(name = "register")
    private val registered = false
    @Column(name = "cancel")
    private val canceled = false
    @Column(name = "delete")
    private val deleted = false
    @Column(name = "info")
    private val info: String? = null
    @Embeddable
    open class RequestPK : Serializable {
        @Column(name = "sample", nullable = false, updatable = false)
        open var sample: Long? = null
        @Column(name = "service", length = 8, nullable = false, updatable = false)
        open var service: String? = null
    }
}