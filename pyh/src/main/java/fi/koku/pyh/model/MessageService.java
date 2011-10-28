package fi.koku.pyh.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//@Service(value = "pyhMessageService")
//public class MessageService implements MessageListener {
//
//  private static Logger log = LoggerFactory.getLogger(MessageService.class);
//
//  private Map<String, Message> messages;
//
//  public MessageService() {
//    messages = new HashMap<String, Message>();
//  }
//
//  /**
//   * TODO: lisää viesti pyh-servicen kautta tietokantaan
//   * 
//   */
//  public synchronized void addMessage(Message m) {
//    messages.put(m.getId(), m);
//    m.addListener(this);
//  }
//  
//  /**
//   * TODO: poista viesti pyh-servicen kautta tietokannasta
//   * 
//   */
//  public synchronized void removeMessage(Message m) {
//    messages.remove(m.getId());
//    m.removeListener(this);
//  }
//  
//  /**
//   * TODO: hae viesti pyh-servicen kautta tietokannasta
//   * 
//   */
//  public synchronized Message getMessage(String id) {
//    return messages.get(id);
//  }
//
//  @Override
//  public void remove(Message m) {
//    removeMessage(m);
//  }
//
//  public void reset() {
//    messages.clear();
//  }
//  
//  /**
//   * TODO: haetaan tietokannasta pyh-servicen kautta käyttäjälle saapuneet viestit
//   * 
//   * -saapunut viesti näkyy käyttäjälle niin kauan kunnes hän on hyväksynyt/hylännyt viestin
//   * 
//   */
//  public List<Message> getMessagesFor(String pic) {
//
//    List<Message> tmp = new ArrayList<Message>();
//    List<Message> results = new ArrayList<Message>();
//    synchronized (this) {
//      tmp.addAll(messages.values());
//    }
//
//    for (Message m : tmp) {
//      if (m.getRecipients().contains(pic) && !m.isReadBy(pic)) {
//        results.add(m);
//      }
//    }
//
//    return results;
//  }
//  
//  /**
//   * TODO: haetaan tietokannasta pyh-servicen kautta käyttäjän lähettämät viestit
//   * 
//   * -lähetetty viesti näkyy käyttäjälle niin kauan kunnes vastaanottaja on hyväksynyt/hylännyt viestin
//   * 
//   */
//  public List<Message> getSentMessages(String pic) {
//
//    List<Message> tmp = new ArrayList<Message>();
//    List<Message> results = new ArrayList<Message>();
//    synchronized (this) {
//      tmp.addAll(messages.values());
//    }
//
//    for (Message m : tmp) {
//      if (m.getFrom().equals(pic)) {
//        results.add(m);
//      }
//    }
//
//    return results;
//  }
//}

