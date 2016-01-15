package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import services.SurveyService;

import domain.Survey;

@RestController
@RequestMapping("/#/vote")

public class SurveyListController {

	
	// Servicios
	@Autowired
	private SurveyService surveyService;
	
	// Método que lista las encuestas
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Survey> surveis;

		surveis = surveyService.allSurveys();

		result = new ModelAndView("#/vote/list");
		System.out.println(surveis + "Hola");
		result.addObject("survey", surveis);

		return result;

	}
	@ModelAttribute("list")
	public Collection<Survey> getInstruments() {
		return surveyService.allSurveys();
	}
}
