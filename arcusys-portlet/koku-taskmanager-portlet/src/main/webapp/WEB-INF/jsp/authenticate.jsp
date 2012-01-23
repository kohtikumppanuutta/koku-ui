<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" %>
<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:defineObjects />
	

<c:choose>
	<c:when test="${not empty requestScope.authenticationURL}">
	  <div class="portlet-section-text">
		Tämä sovellus edellyttää vahvaa tunnistautumista
	  </div>
	  <div class="kks-link">
	  	<a href="${requestScope.authenticationURL}">Klikkaa tästä tunnistautuaksesi vahvasti

	  </div>
	</c:when>
	<c:otherwise>
		<div class="portlet-section-text">
			Kirjaudu järjestelmään
	  	</div>
	</c:otherwise>
</c:choose>

