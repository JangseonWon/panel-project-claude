package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Value
@Accessors(fluent = true)
@Table("request")
@AllArgsConstructor
public class Request {
	@Id
	private final RequestPK id;
}
