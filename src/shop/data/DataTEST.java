package shop.data;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


public class DataTEST {
  @Test
  public void testConstructorAndAttributes() {
    String title1 = "XX";
    String director1 = "XY";
    String title2 = " XX ";
    String director2 = " XY ";
    int year = 2002;

    Video v1 = Data.newVideo(title1, year, director1);
    assertSame(title1, v1.title());
    assertEquals(year, v1.year());
    assertSame(director1, v1.director());

    Video v2 = Data.newVideo(title2, year, director2);
    assertEquals(title1, v2.title());
    assertEquals(director1, v2.director());
  }

  @Test
  public void testConstructorExceptionYear() {
    try {
      Data.newVideo("X", 1800, "Y");
      fail("");
    } catch (IllegalArgumentException e) { }
    try {
      Data.newVideo("X", 5000, "Y");
      fail("");
    } catch (IllegalArgumentException e) { }
    try {
      Data.newVideo("X", 1801, "Y");
      Data.newVideo("X", 4999, "Y");
    } catch (IllegalArgumentException e) {
      fail("");
    }
  }
  @Test
  public void testConstructorExceptionTitle() {
    try {
      Data.newVideo(null, 2002, "Y");
      fail("");
    } catch (IllegalArgumentException e) { }
    try {
      Data.newVideo("", 2002, "Y");
      fail("");
    } catch (IllegalArgumentException e) { }
    try {
      Data.newVideo(" ", 2002, "Y");
      fail("");
    } catch (IllegalArgumentException e) { }
  }
  @Test
  public void testConstructorExceptionDirector() {
    try{
      Data.newVideo("To Kill a MockingBird", 1987, null);
      fail("Director should not be null.");
    }
    catch(IllegalArgumentException e){}
    try{
      Data.newVideo("Terminator", 2004, "");
      fail("Director needs to be non-empty");
    }
    catch(IllegalArgumentException e){}
    try{
      Data.newVideo("Liar Liar", 1997, "Adam Sandler");
    }
    catch(IllegalArgumentException e) {fail("Should be working correctly.");}
  }

  @Test
  public void testToString() {
    String s = Data.newVideo("A",2000,"B").toString();
    assertEquals( s, "A (2000) : B" );
    s = Data.newVideo(" A ",2000," B ").toString();
    assertEquals( s, "A (2000) : B" );
  }

  @Test
  public void testCommandConstructors(){
    class InventoryTrial implements Inventory{
      public int size(){
        return -1;
      }
      public Record get(Video V){
        return null;
      }

      public Iterator<Record> iterator(){
        return null;
      }

      public Iterator<Record> iterator(Comparator<Record> comparator){
        return null;
      }

      public String toString(){
        return "Hello";
      }
    }
    Inventory s =new InventoryTrial();

    try{
      Data.newAddCmd(s,null,7);
      fail("Not the right type of inventory");
    }
    catch (IllegalArgumentException e){ }

    try{
      Data.newInCmd(s,null);
      fail("Not the right type of inventory");
    }
    catch (IllegalArgumentException e){ }

    try{
      Data.newOutCmd(s,null);
      fail("Not the right type of inventory");
    }
    catch (IllegalArgumentException e){ }

    try{
      Data.newClearCmd(s);
      fail("Not the right type of inventory");
    }
    catch (IllegalArgumentException e){ }

    try{
      Data.newRedoCmd(s);
      fail("Not the right type of inventory");
    }
    catch (IllegalArgumentException e){ }

    try{
      Data.newUndoCmd(s);
      fail("Not the right type of inventory");
    }
    catch (IllegalArgumentException e){ }
  }

}
