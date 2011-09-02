package fi.koku.demo;

import javax.portlet.MimeResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.portlet.DispatcherPortlet;
import org.w3c.dom.Element;


/**
 * demonstrate how to set http headers and html head markup.
 * ##TODO## Is this class required anymore?
 * 
 * @author aspluma
 */
public class CustomDispatcherPortlet extends DispatcherPortlet {

  private static final Logger log = LoggerFactory.getLogger(CustomDispatcherPortlet.class);
  
  @Override
  protected void doHeaders(RenderRequest request, RenderResponse response) {
    super.doHeaders(request, response);
    log.debug("CustomDispatcherPortlet: doHeaders");
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
