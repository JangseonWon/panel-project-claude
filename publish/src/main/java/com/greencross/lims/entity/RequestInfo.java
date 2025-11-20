package com.greencross.lims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.experimental.Accessors;

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
