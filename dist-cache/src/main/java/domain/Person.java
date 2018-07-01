package domain;

import java.io.Serializable;

public class Person implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private long id;
  private String name;
  private long created;
  private long updated;
    
  public Person() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public long getCreated() {
    return created;
  }

  public void setCreated(long created) {
    this.created = created;
  }

  public long getUpdated() {
    return updated;
  }

  public void setUpdated(long updated) {
    this.updated = updated;
  }

  @Override
  public String toString() {
    return "Person[" + id + "," + name + "]";
  }

  
}
