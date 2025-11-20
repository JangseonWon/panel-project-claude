package com.greencross.lims.trans;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.entity.Interpretation;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;

import static com.greencross.lims.SecurityConfig.RoleManager;

@UtilityClass
public class AnalysisToDTO {
	public Analysis map(com.greencross.lims.entity.Analysis entity) {
		boolean isManager = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(RoleManager);
		Analysis dto = new Analysis().batch(entity.pk().batch())
									 .row(entity.pk().row())
									 .panel(entity.panel())
									 .createAt(LocalDateTimeToEpoch.map(entity.createTime()))
									 .lastModifyAt(LocalDateTimeToEpoch.map(entity.lastModifyAt()))
									 .serial(entity.serial())
									 .editor(entity.lastModifiedBy()!=null?entity.lastModifiedBy().name():null)
									 .values(entity.value())
									 .result(entity.result())
									 .sample(entity.sample()!=null?entity.sample().id():null)
									 .service(entity.service()!=null?entity.service().id():null)
									 .serviceName(entity.service()!=null?entity.service().name():null)
									 .state(entity.state());
		if(isManager) dto.patientName(entity.sample()!=null&&entity.sample().patient()!=null?entity.sample().patient().name():null);
		else dto.patientName("*");
		if(entity.requests()!=null && entity.requests().interpretations()!=null) {
			Interpretation last = entity.requests().interpretations().stream().max(Comparator.comparing(Interpretation::createTime)).orElse(null);
			if(last!=null) dto.interpretation(last.value())
							  .result(last.value()!=null?(String)last.value().get("result"):null);
		}
		return dto;
	}
}
