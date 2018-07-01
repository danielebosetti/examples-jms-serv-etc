package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;

public class PersonListener 
  implements EntryAddedListener<Object,Object>,
  EntryUpdatedListener<Object, Object>
  {
  
  private static final Logger logger = LoggerFactory.getLogger( "hz.client.PersonList");
  
  @Override
  public void entryAdded(EntryEvent<Object, Object> event) {
    logger.info("added: {}", event);
  }

  @Override
  public void entryUpdated(EntryEvent<Object, Object> event) {
    logger.info("added: {}", event);
  }

}
