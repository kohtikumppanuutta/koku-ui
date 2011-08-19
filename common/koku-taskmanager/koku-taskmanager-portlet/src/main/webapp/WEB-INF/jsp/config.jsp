<%@ include file="init.jsp"%>

<portlet:actionURL  var="configActionURL" >
<portlet:param name="myaction" value="config" />
</portlet:actionURL>
<div>
	<form name="configForm" action="${configActionURL}" method="post" >
		<table class="task-config-table">
			<tr><td><spring:message code="config.taskFilter" /></td><td><input type="text" name="taskFilter" value="<%= taskFilter %>" style="width: 100px;" /></td></tr>
			<tr class="evenRow"><td><spring:message code="config.notifFilter" /></td><td><input type="text" name="notifFilter" value="<%= notifFilter %>" style="width: 100px;" /></td></tr> 
			<tr><td><spring:message code="config.refreshDuration" /></td>
				<td><select name="refreshDuration" id="refreshDuration">
						<option value="10">10s</option>
						<option value="30">30s</option>
						<option value="60">1min</option>
						<option value="900">15min</option>
						<option value="1800">30min</option>
				</select></td>
			</tr>
			<tr class="evenRow"><td><spring:message code="config.openForm" /></td>
				<td><select name="openForm" id="openForm">
						<option value="1" >In portlet</option>
						<option value="2" >New window</option>
						<option value="3">Pop-up</option>
				</select></td>
			</tr>
			<tr><td><input type="submit" value="<spring:message code="config.save"/>" /></td><td></td></tr>
		</table>
	</form>
</div>
<script type="text/javascript">
	// set the default selected option for selectors refreshDuration and openForm
	jQuery('#refreshDuration').val("<%= refreshDuration %>");
	jQuery('#openForm').val("<%= openForm %>");
</script>