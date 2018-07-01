package domain;

public class PersonFactory {
  
  public Person of(long id, String name) {
    long tst = System.currentTimeMillis();
    return of(id, name, tst ) ;
  }

  public Person of(long id, String name, long created) {
    Person res = new Person();
    res.setId(id);
    res.setName(name);
    res.setCreated(created);
    return res ;
  }

}
