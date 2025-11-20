package com.greencross.lims.kanban;

import com.greencross.lims.entity.Issue;
import com.greencross.lims.entity.RequestRareDisease;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@DiscriminatorValue("5fd630a8-f9fc-4a17-872a-5b3e82929d6a")
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(fluent = true)
public class IssueRareDisease extends Issue {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false)
			, @JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)})
	private RequestRareDisease request;
	public IssueRareDisease request(RequestRareDisease request) {
		pk().sample(request.sample().id()).service(request.service().id());
		this.request = request;
		return this;
	}
	public RequestRareDisease request() {
		return request;
	}
}
