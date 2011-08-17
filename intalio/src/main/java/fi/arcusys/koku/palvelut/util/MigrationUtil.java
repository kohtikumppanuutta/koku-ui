package fi.arcusys.koku.palvelut.util;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.http.HttpServletRequest;

/**
 * Porting from Liferay to JBoss - here I've placed all values, related to Liferay's specifics.
 * 
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Apr 15, 2011
 */
public class MigrationUtil {
	public static long getCompanyId(PortletRequest request) {
		//import com.liferay.portal.util.PortalUtil;
//		long companyId = PortalUtil.getCompanyId(request);
		long companyId = 0;
		return companyId;
	}

	public static long getCompanyId(HttpServletRequest request) {
		//import com.liferay.portal.util.PortalUtil;
//		long companyId = PortalUtil.getCompanyId(request);
		long companyId = 0;
		return companyId;
	}

	public static long getCompanyByUser(PortletRequest request) {
//		long userId = Long.parseLong(request.getRemoteUser());
//		User user = UserLocalServiceUtil.getUser(userId);
//		long companyId = user.getCompanyId();
		return 0;
	}
	
	public static String getCompanyWebId(PortletRequest request) {
//		PortalUtil.getCompany(request).getWebId()
		return "test";
	}

	public static String getUser(PortletRequest request) {
//		PortalUtil.getUser(request).getScreenName()
		return request.getUserPrincipal().getName();
	}
	
	public static String getUser(HttpServletRequest request) {
//		PortalUtil.getUser(request).getScreenName()
		return request.getUserPrincipal().getName();
	}
	
	public static Assertion getAssertion(PortletRequest request) {
//		HttpServletRequest req = PortalUtil.getHttpServletRequest(request);
//		Assertion assertion = (Assertion)req.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
		return null;
	}
	
	public static String getServiceURL(PortletRequest request) {
//		String serviceURL = PrefsPropsUtil.getString(PortalUtil.getCompanyId(request), "cas.service.url");
		return "http://localhost:8080/cas";
	}

	public static String getUserPassword(PortletRequest request) {
//		PortalUtil.getUserPassword(request)
		return "test";
//		return getUser(request);
	}
	
	public static boolean hasUserRole(final String user, final long companyId, final String roleName) {
		return true;
	}
	
	public static String getCurrentURL(final HttpServletRequest request) {
		// see com.liferay.portal.util.PortalImpl#getCurrentURL
		StringBuffer sb = request.getRequestURL();

		if (sb == null) {
			sb = new StringBuffer();
		}

		if (request.getQueryString() != null) {
			sb.append("?");
			sb.append(request.getQueryString());
		}

		String completeURL = sb.toString();

		return completeURL;
	}
	
	public static String escapeHtml(final String text) {
		// copied from com.liferay.portal.util.HtmlImpl#escape(String)
		if (text == null) {
			return null;
		}

		if (text.length() == 0) {
			return "";
		}

		// Escape using XSS recommendations from
		// http://www.owasp.org/index.php/Cross_Site_Scripting
		// #How_to_Protect_Yourself

		StringBuilder sb = new StringBuilder(text.length());

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			switch (c) {
				case '<':
					sb.append("&lt;");

					break;

				case '>':
					sb.append("&gt;");

					break;

				case '&':
					sb.append("&amp;");

					break;

				case '"':
					sb.append("&#034;");

					break;

				case '\'':
					sb.append("&#039;");

					break;

				case '�':
					sb.append("&raquo;");

					break;

				default:
					sb.append(c);

					break;
			}
		}

		return sb.toString();
	}
}
