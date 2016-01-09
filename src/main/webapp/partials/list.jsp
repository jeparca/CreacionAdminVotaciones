<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Creamos la tabla para listar las encuestas. -->
<display:table name="survey" id="row" requestURI="survey/list.do"
	pagesize="5" class="displaytag">

	<!-- Mostramos el título de la encuesta -->
	<p>verrde</p>
	<display:column property="survey.title" title="${titleHeader}" />

	<!-- Creamos un boton para acceder a la edicion de la encuesta -->
	<display:column>
		<a href="survey/edit.do?surveyId=${row.id}"> 	<p>Editar</p>
		</a>
	</display:column>

</display:table>

<!-- Creamos un boton para crear una nueva encuesta -->
	<a href="survery/create.do?surveryId=${row.id}"> 	<p>Crear</p>
	</a>
