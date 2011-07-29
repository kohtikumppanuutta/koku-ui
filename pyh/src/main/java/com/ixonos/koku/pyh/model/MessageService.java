package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "pyhMessageService")
public class MessageService implements MessageListener {

  private static Logger log = LoggerFactory.getLogger(MessageService.class);

  private Map<String, Message> messages;

  public MessageService() {
    messages = new HashMap<String, Message>();
  }

  public synchronized void addMessage(Message m) {
    messages.put(m.getId(), m);
    m.addListener(this);
  }

  public synchronized void removeMessage(Message m) {
    messages.remove(m.getId());
    m.removeListener(this);
  }

  public synchronized Message getMessage(String id) {
    return messages.get(id);
  }

  @Override
  public void remove(Message m) {
    removeMessage(m);
  }

  public void reset() {
    messages.clear();
  }

  public List<Message> getMessagesFor(String ssn) {

    List<Message> tmp = new ArrayList<Message>();
    List<Message> results = new ArrayList<Message>();
    synchronized (this) {
      tmp.addAll(messages.values());
    }

    for (Message m : tmp) {
      if (m.getRecipients().contains(ssn)) {
        results.add(m);
      }
    }

    return results;
  }

  public List<Message> getSentMessages(String ssn) {

    List<Message> tmp = new ArrayList<Message>();
    List<Message> results = new ArrayList<Message>();
    synchronized (this) {
      tmp.addAll(messages.values());
    }

    for (Message m : tmp) {
      if (m.getFrom().equals(ssn)) {
        results.add(m);
      }
    }

    return results;
  }
}
