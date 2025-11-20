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
		return Map.of("/panel-service/worlist.html", "Worklist",
					  "/panel-service/dna.html", "DNA",
					  "/panel-service/library.html", "Library",
					  "/panel-service/sequencing.html", "Sequencing");
	}
	@RequestMapping(value="/services", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page[] service() {
		return new Page[]{
				new Page().icon("fa-vial").title("DNA").uri("/panel-service/dna.html").order("3"),
				new Page().icon("fa-vials").title("Library").uri("/panel-service/library.html").order("4"),
				new Page().icon("fa-dna").title("Sequencing").uri("/panel-service/sequencing.html").order("5")
		};
	}
	@RequestMapping(value="/search", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Service search() {
		return null;
	}
}
