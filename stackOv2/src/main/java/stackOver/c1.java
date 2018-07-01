package stackOver;

public class c1 {
  public static void main(String[] args) {
    
    Generic<Object>[] gib;
    gib = new GenericObject[5];
    
    for (Generic<Object> item:gib) {
      System.out.println(item);
    }
  }
}

class Generic<T> {}
class GenericObject extends Generic<Object> {}