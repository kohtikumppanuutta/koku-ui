package com.ixonos.koku.demo;

import javax.portlet.MimeResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.DispatcherPortlet;
import org.w3c.dom.Element;


/**
 * demonstrate how to set http headers and html head markup.
 * @author aspluma
 */
public class CustomDispatcherPortlet extends DispatcherPortlet {

  @Override
  protected void doHeaders(RenderRequest request, RenderResponse response) {
    super.doHeaders(request, response);
    System.out.println("CustomDispatcherPortlet: doHeaders");
    response.addProperty("x-myheader", "hello");
    
    Element e = response.createElement("title");
    e.setTextContent("my dynamically set title");
    response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, e);
    
    Element jsElement = response.createElement("script");
    jsElement.setAttribute("src", response.encodeURL((request.getContextPath() + "/js/bookCatalog.js")));
    jsElement.setAttribute("type", "text/javascript");
    response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jsElement);
  }

}
