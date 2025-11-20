package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@Accessors(fluent = true)
public class RequestInfo implements Serializable {
	@Column
	private String code;
	@Column
	private String value;
	@Column(name = "\"desc\"")
	private String desc;
}
