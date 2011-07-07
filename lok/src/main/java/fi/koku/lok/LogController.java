package fi.koku.lok;

import javax.portlet.RenderRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Controller for log management (LOK).
 * 
 * @author aspluma
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogController {

  @RenderMapping
  public String render(RenderRequest req, Model model) {
    return "hello";
  }

}
