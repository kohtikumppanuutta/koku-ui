package fi.koku.pyh.ui.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Portlet controller for demonstrating technical solutions.
 * 
 * @author aspluma
 */
@Controller
@RequestMapping("VIEW")
public class DemoController {
  private static Logger log = LoggerFactory.getLogger(DemoController.class);

  @RequestMapping
  public String showMessages1(Model model) {
    System.out.println("showMessages1");

    // demonstrate HTTP request parameter access.
    // can only be done in a GateIn specific manner, non-portable.
    // PortalRequestContext reqCtx = Util.getPortalRequestContext();
    // HttpServletRequest req = reqCtx.getRequest();
    // System.out.println("params: "+req.getParameterMap().keySet());

    // if (!model.containsAttribute("msg"))
    // model.addAttribute("msg", "hello, world (log): " +
    // req.getParameter("foo"));
    log.debug("run");
    return "message";
  }

}
