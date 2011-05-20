<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaLapset" />
</portlet:renderURL>
<portlet:actionURL var="lisaaActionUrl">
	<portlet:param name="toiminto" value="lisaaTieto" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:actionURL>
<portlet:actionURL var="hakuActionUrl">
	<portlet:param name="toiminto" value="hae" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:actionURL>


<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" />
		</a>
	</div>

</div>

<div id="main" class="wide">
	<h1>
		${lapsi.nimi}
		<spring:message code="ui.kks.otsikko" />
	</h1>

	<div id="page">
		<table width="100%" border="0">
			<tr>
				<th>TIETOKOKOELMA
				</td>
				<th>VIIMEISIN KIRJAUS
				<th>KIRJAUSTEN TILA
			</tr>

		<c:if test="${not empty tiedot}">
			<c:forEach var="tieto" items="${tiedot}">
			<tr>
				<td><span class="kokoelma">
				
				<a
						href="
						<portlet:actionURL>
							<portlet:param name="toiminto" value="naytaKehitystieto" />
							<portlet:param name="hetu" value="${tieto.tyyppi}" />
						</portlet:actionURL>">
						<strong>${ tieto.nimi }</strong> </a> 
						
				</span>
				</td>
				<td>${ tieto.muokkaaja } ${ tieto.muokkausPvm }</td>
				<td>
					<c:choose>
				<c:when test="${tieto.tila.aktiivinen}">
						Aktiivinen
					</c:when>
					<c:otherwise>
						Lukittu
					</c:otherwise>
						</c:choose>
				</td>
			</tr>
			</c:forEach>
			</c:if>

		</table>
		<p>
			<br> AKTIVOI UUSI TIETOKOKOELMA<span class="uusi">VALITSE
				KOKOELMA: <select name="select3" class="kokoelmavalinta">
					<option></option>
					<option>esiopetukseen siirtyminen</option>

					<option>kouluun siirtyminen</option>
					<option>tuen tarpeen muutokset</option>
			</select> </span>AKTIIVINEN KIRJAUSAIKA: Alkaa: <select name="select"
				class="syntmaika">
				<option>01</option>
				<option>02</option>
				<option>03</option>

				<option>04</option>
				<option>05</option>
				<option>06</option>
				<option>07</option>
				<option>08</option>
				<option>09</option>

				<option>10</option>
				<option>11</option>
				<option>12</option>
				<option>13</option>
				<option>14</option>
				<option>15</option>

				<option>16</option>
				<option>17</option>
				<option>18</option>
				<option>19</option>
				<option>20</option>
				<option>21</option>

				<option>22</option>
				<option>23</option>
				<option>24</option>
				<option>25</option>
				<option>26</option>
				<option>27</option>

				<option>28</option>
				<option>29</option>
				<option>30</option>
				<option>31</option>
			</select> <select name="select2" class="syntmaika">
				<option>01</option>

				<option>02</option>
				<option>03</option>
				<option>04</option>
				<option>05</option>
				<option>06</option>
				<option>07</option>

				<option>08</option>
				<option>09</option>
				<option>10</option>
				<option>11</option>
				<option>12</option>
			</select> <select name="select2" class="syntmaika">
				<option>2010</option>
				<option>2011</option>
				<option>2012</option>
				<option>2013</option>
				<option>2014</option>

				<option>2015</option>
				<option>2016</option>
				<option>2017</option>
				<option>2018</option>
				<option>2019</option>
			</select> - Loppuu: <select name="select2" class="syntmaika">
				<option>01</option>
				<option>02</option>
				<option>03</option>
				<option>04</option>
				<option>05</option>

				<option>06</option>
				<option>07</option>
				<option>08</option>
				<option>09</option>
				<option>10</option>
				<option>11</option>

				<option>12</option>
				<option>13</option>
				<option>14</option>
				<option>15</option>
				<option>16</option>
				<option>17</option>

				<option>18</option>
				<option>19</option>
				<option>20</option>
				<option>21</option>
				<option>22</option>
				<option>23</option>

				<option>24</option>
				<option>25</option>
				<option>26</option>
				<option>27</option>
				<option>28</option>
				<option>29</option>

				<option>30</option>
				<option>31</option>
			</select> <select name="select2" class="syntmaika">
				<option>01</option>
				<option>02</option>
				<option>03</option>

				<option>04</option>
				<option>05</option>
				<option>06</option>
				<option>07</option>
				<option>08</option>
				<option>09</option>

				<option>10</option>
				<option>11</option>
				<option>12</option>
			</select> <select name="select2" class="syntmaika">
				<option>2010</option>
				<option>2011</option>

				<option>2012</option>
				<option>2013</option>
				<option>2014</option>
				<option>2015</option>
				<option>2016</option>
				<option>2017</option>

				<option>2018</option>
				<option>2019</option>
			</select>
		</p>
		<span class="viestintiedot"> <input type="submit"
			class="tallenna" value="Aktivoi kokolema"> </span>

	</div>
</div>





<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		$(".tietokentta").hide();

		$("a.tieto").click(function() {
			$(this).toggleClass("active").next().slideToggle("fast");
		});

	});
</script>