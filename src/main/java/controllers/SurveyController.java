package controllers;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;


import main.java.Authority;
import main.java.AuthorityImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import services.SurveyService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.CheckToken;
import domain.Survey;

@RestController
@RequestMapping("/vote")
public class SurveyController {

	// Servicios
	@Autowired
	private SurveyService surveyService;
	private static Integer tokenj;
	// M�todos

	// M�todo para guardar la votaci�n creada.
	@RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody Survey save(@RequestBody String surveyJson,
			@CookieValue("user") String user, @CookieValue("token") String token)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Survey s = mapper.readValue(surveyJson, Survey.class);

		CheckToken isValid = new CheckToken();
		ObjectMapper checkToken = new ObjectMapper();
		//Aqui hay que pasar la url de autentificacion
		isValid = checkToken.readValue(new URL(
				"http://auth-egc.azurewebsites.net/Help/api/checkToken?token=" + token),
				domain.CheckToken.class);
		Assert.isTrue(isValid.getValid());
		int i = surveyService.save(s);
		Survey res = surveyService.findOne(i);
		Authority a = new AuthorityImpl();
		tokenj= CheckToken.calculateToken(res.getId());
		a.postKey(String.valueOf(res.getId()),tokenj);
		
		return res;
	}

	// M�todo para guardar la votaci�n con el censo. Relaci�n con
	// CREACION/ADMINISTRACION DE CENSO.
	@RequestMapping(value = "/saveCensus", method = RequestMethod.GET)
	public @ResponseBody void saveCensus(@RequestParam Integer surveyId,
			@RequestParam Integer censusId) throws JsonParseException,
			JsonMappingException, IOException {
		surveyService.addCensus(surveyId, censusId);
	}

	@RequestMapping(value = "/getcookies", method = RequestMethod.GET)
	public @ResponseBody String cookie(@CookieValue("user") String user,
			@CookieValue("token") String token) {
		return "{\"user\":\"" + user + "\", \"token\":\"" + token + "\"}";
	}

	// M�todo que devuelve la lista de votaciones creadas para editarlas.
	// Relaci�n con CREACION/ADMINISTRACION DE CENSO.
	@RequestMapping(value = "/mine", method = RequestMethod.GET)
	public Collection<Survey> findAllSurveyByCreator(
			@CookieValue("user") String user, @CookieValue("token") String token) 
			throws JsonParseException, JsonMappingException, IOException{
		String creator = user;
		CheckToken isValid = new CheckToken();
		ObjectMapper checkToken = new ObjectMapper();
		isValid = checkToken.readValue(new URL(
				"http://auth-egc.azurewebsites.net/Help/api/checkToken?token=" + token),
				domain.CheckToken.class);
		Assert.isTrue(isValid.getValid());
		Collection<Survey> res = surveyService.allCreatedSurveys(creator);
		return res;
	}

	// M�todo que devuelve la lista de votaciones finalizadas. Relaci�n con
	// VISUALIZACION.
	@RequestMapping(value = "/finishedSurveys", method = RequestMethod.GET)
	public Collection<Survey> findAllfinishedSurveys() {
		Collection<Survey> res = surveyService.allFinishedSurveys();
		return res;
	}
	
	// M�todo que devuelve la lista completa de finalizadas. Relaci�n con
	// VISUALIZACION.
	@RequestMapping(value = "/allSurveys", method = RequestMethod.GET)
	public Collection<Survey> findAllSurveys() {
		Collection<Survey> res = surveyService.allSurveys();
		return res;
	}

	// M�todo que borra una votaci�n tras comprobar que no tiene censo
	// relacionado.
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void delete(@RequestParam int id) {
		surveyService.delete(id);
	}

	// M�todo devuelve una survey para realizar una votaci�n. Relaci�n con
	// CABINA DE VOTACION
	@RequestMapping(value = "/survey", method = RequestMethod.GET)
	public Survey getSurvey(@RequestParam int id) {
		Survey s = surveyService.findOne(id);
		return s;
	}
	
	//crear una votacion
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Survey survey;

		survey = surveyService.create();

		result = new ModelAndView("vote/create");

		result.addObject("survey", survey);
		

		return result;
	}
}