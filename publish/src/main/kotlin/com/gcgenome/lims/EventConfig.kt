package com.gcgenome.lims

import com.gcgenome.lims.test.mrd.TestInfo
import com.gcgenome.lims.test.tmp.Ballondor
import com.gcgenome.lims.test.tmp.S051
import com.gcgenome.lims.workflow.*
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Configuration
@EnableAspectJAutoProxy
open class EventConfig(@PersistenceContext(unitName = "LIMS") val lims: EntityManager) {
    companion object {
        private const val source = "panel-publish"
        private val process = EventProcess.FINISHED
    }

    @Publish
    fun publish(user: String, report: com.greencross.lims.entity.Report): List<Event> {
        val createAt = LocalDateTime.now()
        val request = report.interpretation().request()
        val test = toTestInfo(report.interpretation().request().pk().service())
        val temp = if (test != null) mapOf("test" to test) else mapOf()
        val param = temp.toMutableMap().asParam("publish")
        return listOf(
            Event(
                id = UUID.randomUUID(),
                timestamp = createAt,
                request = map(request),
                source = source,
                process = process,
                type = EventType.COMPLETE,
                param = param,
                user = user,
            ),
        )
    }
    private fun toTestInfo(service: String): Any? {
        for (t in com.gcgenome.lims.test.bloodcancer.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.dgs.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.genomescreen.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.hrd.TestInfo.TESTS) if (service == t.code()) return t
        for (t in TestInfo.TESTS) if (service == t.code()) return t
        for (t in TestInfo.SCREEN_TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.panel.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.sanger.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.single.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.solidtumor2.TestInfo.TESTS) if (service == t.code()) return t
        for (t in com.gcgenome.lims.test.wes.TestInfo.TESTS) if (service == t.code()) return t
        for (t in Ballondor.TESTS) if (service == t.code()) return t
        if (service.equals(S051.instance)) return S051.instance
        return null
    }

    private fun map(request: com.greencross.lims.entity.Request): Request = Request(
        id = "${request.pk().sample()}:${request.pk().service()}",
        service = Service(request.pk().service(), request.service().name() ?: ""),
        samples = listOf(map(request.sample(), request.dateSampling())),
        sample = map(request.sample(), request.dateSampling()),
        requester = Organization(request.sample().patient().customerCode() ?: "", request.sample().patient().customerName() ?: ""),
        dateRequest = request.dateRequest(),
        dateReception = request.dateReception(),
        dateDuePublish = request.dateDuePublish(),
    )
    private fun map(sample: com.greencross.lims.entity.Sample, dateSampling: LocalDate?): Sample = Sample(
        id = sample.id(),
        type = sample.sampleType() ?: "",
        patient = map(sample.patient()),
        dateSampling = dateSampling,
        age = null,
        remark = sample.remark(),
    )
    private fun map(patient: com.greencross.lims.entity.Patient): Patient = Patient(
        organization = if (patient.customerCode2() != null) {
            Organization(patient.customerCode2(), patient.customerName2() ?: "")
        } else {
            Organization(patient.customerCode() ?: "", patient.customerName() ?: "")
        },
        name = patient.name() ?: "",
        birth = patient.birth()?.let { map(patient.birth()) },
        sex = patient.sex()?.let { Patient.Companion.Sex.valueOf(patient.sex().name) },
        mrn = patient.mrn(),
    )
    private fun map(date: LocalDate): Patient.Companion.Birth = Patient.Companion.Birth(
        year = date.year,
        month = date.monthValue,
        day = date.dayOfMonth,
    )
}
