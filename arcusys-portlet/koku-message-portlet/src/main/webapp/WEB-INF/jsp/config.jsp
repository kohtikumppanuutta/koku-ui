<%@ include file="init.jsp"%>

<portlet:actionURL  var="configActionURL" >
<portlet:param name="myaction" value="config" />
</portlet:actionURL>
<div>
	<form name="configForm" action="${configActionURL}" method="post" >
		<table class="task-config-table">
			<tr>
				<td><spring:message code="config.refreshDuration" /></td>
				<td>
					<select name="refreshDuration" id="refreshDuration">
						<option value="10">10s</option>
						<option value="30">30s</option>
						<option value="60">1min</option>
						<option value="900">15min</option>
						<option value="1800">30min</option>
					</select>
				</td>
			</tr>
			<tr class="evenRow">
				<td><spring:message code="config.messageType" /></td>
				<td>
					<select name=messageType id="messageType">
						<option value="1" >Inbox</option>
						<option value="2" >Outbox</option>
						<option value="3">Archive</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><spring:message code="config.portletPath" /></td>
				<td>
					<textarea style="width: 200px" rows="1" name="<%= Constants.PREF_MESSAGE_PORTLET_PATH %>" id="<%= Constants.PREF_MESSAGE_PORTLET_PATH %>"></textarea>				
				</td>
			</tr>
			<tr>
				<td>
					<input type="submit" value="<spring:message code="config.save"/>" />
				</td>
				<td></td>
			</tr>
		</table>
					
			
	</form>
</div>
<script type="text/javascript">
	// set the default selected option for selectors refreshDuration and openForm
	jQuery('#refreshDuration').val("<%= refreshDuration %>");
	jQuery('#messageType').val("<%= messageType %>");
	jQuery('#<%= Constants.PREF_MESSAGE_PORTLET_PATH %>').val("<%= portletPath %>");
</script>