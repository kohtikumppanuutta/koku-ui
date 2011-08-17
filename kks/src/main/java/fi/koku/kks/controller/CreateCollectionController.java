package fi.koku.kks.controller;

import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import fi.koku.kks.model.CollectionState;
import fi.koku.kks.model.Creatable;
import fi.koku.kks.model.Creation;
import fi.koku.kks.model.DemoService;
import fi.koku.kks.model.KKSCollection;
import fi.koku.kks.model.Person;
import fi.koku.kks.ui.common.State;

/**
 * Controller for creating agreements and plans
 * 
 * @author tuomape
 */
@Controller(value = "createCollectionController")
@RequestMapping(value = "VIEW")
public class CreateCollectionController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(CreateCollectionController.class);

  @ActionMapping(params = "action=createNewVersion")
  public void createVersion(@ModelAttribute(value = "child") Person child, @RequestParam String id,
      @RequestParam String name, @RequestParam(required = false) String clean, ActionResponse response,
      SessionStatus sessionStatus) {

    log.debug("create new version");

    Creatable a = new Creatable(id, true, name);
    a.setCopyContent(!Boolean.valueOf(clean));
    KKSCollection collection = demoService.luocollection(child, name, a);

    if (collection != null) {
      CollectionState tila = collection.getState();
      tila.setState(State.ACTIVE);
    }
    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection.getId());

    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=createCollection")
  public void create(@ModelAttribute(value = "child") Person child,
      @ModelAttribute(value = "creation") Creation creation, BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {

    log.debug("create collection");

    Creatable a = Creatable.create(creation.getField());
    String nimi = "".equals(creation.getName()) ? a.getName() : creation.getName();
    KKSCollection collection = demoService.luocollection(child, nimi, a);

    if (collection != null) {
      CollectionState tila = collection.getState();
      tila.setState(State.ACTIVE);
    }
    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());
    creation = getCommandObject();
    creation.setName("");
    creation.setField("");
    sessionStatus.setComplete();
  }

  @ModelAttribute("child")
  public Person getchild(@RequestParam String pic) {
    log.info("getchild");
    return demoService.searchChild(pic);
  }

  @ModelAttribute("creation")
  public Creation getCommandObject() {
    log.debug("get creation command object");
    return new Creation();
  }

  @ActionMapping(params = "action=activate")
  public void activate(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {
    KKSCollection k = child.getKks().getCollection(collection);

    // activate the collection for a given time period
    log.debug("activate collection");

    if (k != null) {
      CollectionState tila = k.getState();
      tila.setState(State.ACTIVE);
    }

    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=lock")
  public void lock(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {
    KKSCollection k = child.getKks().getCollection(collection);

    // activate the collection for a given time period
    log.debug("activate collection");

    if (k != null) {
      CollectionState tila = k.getState();
      tila.setState(State.LOCKED);
    }

    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());
    sessionStatus.setComplete();
  }
}
