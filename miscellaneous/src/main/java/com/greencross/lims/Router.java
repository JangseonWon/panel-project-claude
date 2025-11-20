package com.greencross.lims;

import com.greencross.lims.dto.Page;
import com.greencross.lims.dto.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Router {
	@RequestMapping(value="/actuator/routes", method= RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> routes() {
		return Map.of("/panel-service/misc.html", "Misc.");
	}
	@RequestMapping(value="/services", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page[] service() {
		return new Page[]{
			new Page().icon("fa-database").title("Misc.").uri("/panel-service/misc.html").order("7")
		};
	}
	@RequestMapping(value="/search", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Service search() {
		return null;
	}
}
