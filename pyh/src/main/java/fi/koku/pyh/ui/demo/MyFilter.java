/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.ui.demo;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;


public class MyFilter implements PortletFilter, RenderFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws PortletException {
		System.out.println("init");
	}

	@Override
	public void doFilter(RenderRequest req, RenderResponse res, FilterChain chain)
	throws IOException, PortletException {
		System.out.println("pv: "+req.getPrivateParameterMap().entrySet());
		System.out.println("p: "+req.getParameterMap().entrySet());
		System.out.println("pp: "+req.getPublicParameterMap().entrySet());
		chain.doFilter(req, res);
	}

}
