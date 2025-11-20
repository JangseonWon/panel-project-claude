package com.greencross.lims;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Router {
	@RequestMapping(value="/services", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page[] services() {
		return new Page[] {
			new Page().icon("fa-comment-medical").title("Reading").uri("/panel-service/reading.html").order("6")
		};
	}
	@Setter
	@Accessors(fluent = true)
	public static class Page {
		private String icon;
		private String title;
		private String uri;
		private String order;
	}
}
