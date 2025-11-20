package com.greencross.alis.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(fluent = true)
@Builder
public class Request implements Serializable {
	private final LocalDate requestDate;
	private final int requestNo;
	private final long requestNo2;
	private final String itemCode;
}
