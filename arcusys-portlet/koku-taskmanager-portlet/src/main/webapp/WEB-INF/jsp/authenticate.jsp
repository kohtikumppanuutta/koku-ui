<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" %>
<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:defineObjects />
	

<c:choose>
	<c:when test="${not empty requestScope.authenticationURL}">
	  <div class="portlet-section-text">
		T�m� sovellus edellytt�� vahvaa tunnistautumista
	  </div>
	  <div class="kks-link">
	  	<a href="${requestScope.authenticationURL}">Klikkaa t�st� tunnistautuaksesi vahvasti

	  </div>
	</c:when>
	<c:otherwise>
		<div class="portlet-section-text">
			Kirjaudu j�rjestelm��n
	  	</div>
	</c:otherwise>
</c:choose>

