package fi.koku.kks.controller;

import java.util.Date;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import org.apache.commons.lang.StringUtils;

import fi.koku.kks.model.DemoService;
import fi.koku.kks.model.Entry;
import fi.koku.kks.model.EntryType;
import fi.koku.kks.model.KKSCollection;
import fi.koku.kks.model.Person;

/**
 * Controller for managing collection showing and value setting
 * 
 * @author tuomape
 * 
 */
@Controller(value = "collectionController")
@RequestMapping(value = "VIEW")
public class CollectionController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static final Logger LOG = LoggerFactory.getLogger(CollectionController.class);

  @RenderMapping(params = "action=showCollection")
  public String show(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, RenderResponse response, Model model) {
    LOG.info("show collection");
    model.addAttribute("child", child);
    model.addAttribute("collection", child.getKks().getCollection(collection));
    return "collection";
  }

  @ModelAttribute("child")
  public Person getchild(@RequestParam(value = "pic") String pic) {
    return demoService.searchChild(pic);
  }

  @ActionMapping(params = "action=saveCollection")
  public void save(@ModelAttribute(value = "child") Person child, @ModelAttribute(value = "entry") KKSCollection entry,
      BindingResult bindingResult, @RequestParam(required = false) String multiValueId,
      @RequestParam(required = false) String type, ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("save collection");
    child.getKks().removeCollection(entry);
    child.getKks().addCollection(entry);

    if (StringUtils.isNotBlank( type )) {

      response.setRenderParameter("action", "showMultivalue");
      response.setRenderParameter("pic", child.getPic());
      response.setRenderParameter("collection", entry.getId());
      response.setRenderParameter("entryType", type);

      if (multiValueId != null && !"".equals(multiValueId)) {
        response.setRenderParameter("entryId", multiValueId);
      }
    } else {
      response.setRenderParameter("action", "showChild");
      response.setRenderParameter("pic", child.getPic());
    }
    sessionStatus.setComplete();
  }

  @ModelAttribute("entry")
  public KKSCollection getCommandObject(@RequestParam(value = "collection") String collection,
      @RequestParam(value = "pic") String pic) {
    LOG.debug("get command object");

    Person child = demoService.searchChild(pic);
    return child.getKks().getCollection(collection);
  }

  @ActionMapping(params = "action=addMultivalue")
  public void saveMultivalue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "entryType") String entryType, @RequestParam(value = "collection") String collection,
      @RequestParam(value = "entryId", required = false) String entry, @RequestParam(value = "value") String value,
      ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("save multivalue");

    KKSCollection kok = child.getKks().getCollection(collection);

    if (entry != null && !"".equals(entry)) {
      Entry k = kok.getEntry(entry);
      k.setValue(value);
    } else {
      EntryType t = kok.getType().getEntryType(entryType);
      if (t != null) {
        Entry k = new Entry(value, new Date(), 1, t.getRegister(), "Kaisa Kirjaaja", t);
        kok.addMultivalue(k);
      }

    }
    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=removeMultivalue")
  public void removeMultiValue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, @RequestParam(value = "entryId") String entry,
      ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("remove multivalue");

    KKSCollection kok = child.getKks().getCollection(collection);

    if (entry != null && !"".equals(entry)) {
      kok.removeMultivalue(entry);
    }

    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=cancelMultivalue")
  public void cancelMultiValue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("cancel multivalue");

    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "action=showMultivalue")
  public String showMultivalue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection,
      @RequestParam(value = "entryType", required = false) String entryType,
      @RequestParam(value = "entryId", required = false) String entry, RenderResponse response, Model model) {
    LOG.info("show collection");

    KKSCollection kok = child.getKks().getCollection(collection);
    EntryType t = kok.getType().getEntryType(entryType);

    model.addAttribute("child", child);
    model.addAttribute("collection", kok);
    model.addAttribute("type", t);

    if (entry != null && !"".equals(entry)) {
      model.addAttribute("entryvalue", kok.getEntry(entry));
    }
    return "multivalue";
  }
}
