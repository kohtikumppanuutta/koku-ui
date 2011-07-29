package com.ixonos.koku.pyh;

import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.ixonos.koku.pyh.model.Message;
import com.ixonos.koku.pyh.model.MessageService;

@Controller(value = "messageController")
@RequestMapping(value = "VIEW")
public class MessageController {

  private static Logger log = LoggerFactory.getLogger(MessageController.class);

  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;

  @Autowired
  @Qualifier(value = "pyhMessageService")
  private MessageService messageService;

  @ActionMapping(params = "action=acceptMessage")
  public void accept(@RequestParam String messageId, ActionResponse response) {
    Message m = messageService.getMessage(messageId);
    m.accept();
    response.setRenderParameter("action", "guardianFamilyInformation");
  }

  @ActionMapping(params = "action=rejectMessage")
  public void reject(@RequestParam String messageId, ActionResponse response) {
    Message m = messageService.getMessage(messageId);
    m.reject();
    response.setRenderParameter("action", "guardianFamilyInformation");
  }

}
