<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="survey" id="row" requestURI="survey/list.do"
		pagesize="5" class="displaytag">

			<display:column>
				<a
					href="survey/edit.do?surveyId=${row.id}">
					<spring:message code="survey.edit" />
				</a>
			</display:column>
		
			<spring:message code="survey.title" var="titleHeader" />
			<display:column property="survey.title" title="${titleHeader}" />
			
			</display:table>
