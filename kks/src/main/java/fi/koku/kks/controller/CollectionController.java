package fi.koku.kks.controller;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
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

import fi.koku.kks.model.Entry;
import fi.koku.kks.model.KKSCollection;
import fi.koku.kks.model.KksService;
import fi.koku.kks.model.Person;
import fi.koku.services.entity.kks.v1.KksEntryClassType;

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
  @Qualifier("kksService")
  private KksService kksService;

  private static final Logger LOG = LoggerFactory.getLogger(CollectionController.class);

  @RenderMapping(params = "action=showCollection")
  public String show(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, RenderResponse response, Model model) {
    LOG.info("show collection");
    model.addAttribute("child", child);
    model.addAttribute("collection", kksService.getKksCollection(collection));
    return "collection";
  }

  @ModelAttribute("child")
  public Person getchild(@RequestParam(value = "pic") String pic) {
    return kksService.searchChild(pic);
  }

  @ActionMapping(params = "action=saveCollection")
  public void save(@ModelAttribute(value = "child") Person child, @ModelAttribute(value = "entry") KKSCollection entry,
      BindingResult bindingResult, @RequestParam(required = false) String multiValueId,
      @RequestParam(required = false) String type, @RequestParam(value = "valueId", required = false) String valueId,
      ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("save collection");

    System.out.println(" SAVE ");
    for (Entry e : entry.getEntries().values()) {
      System.out.println("Entry: " + e.getId() + " " + e.getType().getName() + " Value " + e.getValue());
    }

    kksService.updateKksCollection(entry, child.getPic());

    if (StringUtils.isNotBlank(type)) {

      response.setRenderParameter("action", "showMultivalue");
      response.setRenderParameter("pic", child.getPic());
      response.setRenderParameter("collection", entry.getId());
      response.setRenderParameter("entryType", type);
      response.setRenderParameter("valueId", valueId == null ? "" : valueId);

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

    KKSCollection k = kksService.getKksCollection(collection);

    System.out.println(" COMMAND ");
    for (Entry e : k.getEntries().values()) {
      System.out.println("Entry: " + e.getId() + " " + e.getType().getName() + " Value " + e.getValue());
    }
    return k;
  }

  @ActionMapping(params = "action=addMultivalue")
  public void saveMultivalue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "entryType") String entryType, @RequestParam(value = "collection") String collection,
      @RequestParam(value = "entryId", required = false) String entry, @RequestParam(value = "value") String value,
      @RequestParam(value = "valueId") String valueId, ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("save multivalue");

    kksService.addKksEntry(child.getPic(), entry, valueId, value);
    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=removeMultivalue")
  public void removeMultiValue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, @RequestParam(value = "entryId") String entry,
      @RequestParam(value = "valueId") String valueId, ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("remove multivalue");
    kksService.removeKksEntry(child.getPic(), entry, valueId, "");
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
      @RequestParam(value = "entryId", required = false) String entry,
      @RequestParam(value = "valueId", required = false) String valueId, RenderResponse response, Model model) {
    LOG.info("show collection");

    KKSCollection kok = kksService.getKksCollection(collection);

    KksEntryClassType t = kksService.getEntryClassType(entryType);

    model.addAttribute("child", child);
    model.addAttribute("collection", kok);
    model.addAttribute("type", t);
    model.addAttribute("valueId", valueId);

    if (StringUtils.isNotEmpty(entry)) {
      model.addAttribute("entryvalue", kok.getEntry(entry));
    }

    return "multivalue";
  }
}
