package shop.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import shop.command.UndoableCommand;
import shop.command.CommandHistory;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


public class InventoryTEST {


  final VideoObj va = new VideoObj( "A", 2001, "A" );
  final VideoObj vb = new VideoObj( "B", 2000, "B" );
  final VideoObj vc = new VideoObj("C", 1999, "C");
  @Test
  public void testSize() {
     InventorySet s = new InventorySet();
        assertEquals( 0, s.size() );
        s.addNumOwned(va,  1); assertEquals( 1, s.size() );
        s.addNumOwned(va,  2); assertEquals( 1, s.size() );
        s.addNumOwned(vb,  1); assertEquals( 2, s.size() );
        s.addNumOwned(vb, -1); assertEquals( 1, s.size() );
        s.addNumOwned(va, -3); assertEquals( 0, s.size() );
        try { s.addNumOwned(va, -3); } catch ( IllegalArgumentException e ) {}
        assertEquals( 0, s.size() );
      }

  @Test
  public void testAddNumOwned() {
    InventorySet s = new InventorySet();
    assertEquals( 0, s.size() );
    Record r;
    r = s.addNumOwned(va, 1);
    assertEquals( va, (s.get(va)).video() );
    assertEquals( 1, s.get(va).numOwned());
    assertNull(r);
    try{
      s.addNumOwned(null,87);
      fail("Can't add null");
    }
    catch (IllegalArgumentException e){

    }
    try{
      s.addNumOwned(va,0);
      fail("Can't add zero");
    }
    catch(IllegalArgumentException e){ }

    try{
      s.addNumOwned(va,-2);
      fail("can't remove more than own");
    }
    catch(IllegalArgumentException e){
      assertEquals(e.getMessage(), "Not enough Videos");
    }

    s.addNumOwned(va, 6);

    try{
      s.addNumOwned(va,-6);
      assertEquals(s.get(va).numOwned(),1);
    }
    catch (IllegalArgumentException e){
      fail("Should work");
    }
    Record z = s.get(va);
    r = s.addNumOwned(va, 6);
    assertSame(z,r);
    s.checkOut(va);
    s.checkOut(va);

    try{
      s.addNumOwned(va,-6);
      fail("Too many checked out");
    }
    catch (IllegalArgumentException e){
      assertEquals(e.getMessage(), "Can't remove checkedout videos");
    }

    s.addNumOwned(vb, 7);
    s.addNumOwned(vb,-7);
    r=s.addNumOwned(va,-5);
    assertEquals(r.numOwned(),7);
    assertEquals(s.get(va).numOwned(),2);

    assertEquals(s.size(),1);
  }
  @Test
  public void testCheckOutCheckIn() {
    InventorySet s = new InventorySet();

    s.addNumOwned(vb, 3);


    try{
      s.checkOut(new VideoObj("D", 2008, "D"));
    }
    catch (IllegalArgumentException e){
      assertEquals(e.getMessage(),"Video not found");
    }

    try{
      s.checkIn(new VideoObj("D", 2008, "D"));
    }
    catch (IllegalArgumentException e){
      assertEquals(e.getMessage(),"Video not found");
    }

    s.checkOut(vb); s.checkOut(vb); s.checkOut(vb);

    assertEquals(s.get(vb).numOut(),3);
    s.checkIn(vb);
    assertEquals(s.get(vb).numOut(),2);
    s.checkOut(vb);
    assertEquals(s.get(vb).numRentals(),4);

    try{
      s.checkOut(vb);
      fail("There are no videos to check out");
    }
    catch (IllegalArgumentException e){
      assertEquals(e.getMessage(),"Video not in stock");
    }

    s.checkIn(vb); s.checkIn(vb); s.checkIn(vb);

    try{
      s.checkIn(vb);
      fail("There are no videos to check back in");
    }
    catch (IllegalArgumentException e){
      assertEquals(e.getMessage(), "No videos to return");
    }
  }
  @Test
  public void testClear() {
    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    s.addNumOwned(vb, 7);
    s.addNumOwned(vc,8);
    assertEquals(s.size(),3);
    s.clear();
    assertEquals(s.size(),0);

    s.addNumOwned(va,4);
    s.addNumOwned(vb, 7);
    s.addNumOwned(vc,8);
    assertEquals(s.size(),3);
    Map<Video, Record> m =s.clear();
    assertTrue(m.containsKey(va));
    assertTrue(m.containsKey(vb));
    assertTrue(m.containsKey(vc));
    assertEquals(m.get(va).numOwned(),4);
    assertEquals(m.get(vb).numOwned(),7);
    assertEquals(m.get(vc).numOwned(),8);

    assertEquals(s.size(),0);
  }
  @Test
  public void testGet() {
    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    s.addNumOwned(vb, 7);
    s.addNumOwned(vc,8);

    assertEquals(s.get(va).video(),va);
    s.checkOut(vb);
    assertEquals(s.get(vb).numOut(),1);
    assertEquals(s.get(vb).numRentals(),1);
    assertEquals(s.get(vb).numOwned(),7);
  }
  @Test
  public void testReplaceEntry(){
    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    s.addNumOwned(vb, 7);
    s.addNumOwned(vc,8);
    Record r =s.addNumOwned(va, 6);
    assertEquals(s.get(va).numOwned(),10);
    s.replaceEntry(va,r);
    assertEquals(s.get(va).numOwned(),4);
  }

  @Test
  public void testReplaceMap(){
    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    Map<Video,Record> l = s.clear();
    s.addNumOwned(vb,7);
    assertEquals(s.get(vb).numOwned(),7);
    s.replaceMap(l);
    assertEquals(s.get(va).numOwned(),4);
    assertNull(s.get(vb));

  }
  @Test
  public void testGetHistory(){
    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    CommandHistory ch = s.getHistory();
    assertNotNull(ch);
  }
  @Test
  public void testIterator1() {
    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    s.addNumOwned(vb, 7);
    s.addNumOwned(vc,8);
    Iterator<Record> ir = s.iterator();
    int i=0;
    while(ir.hasNext()){
      i++;
      Record r =ir.next();
//      System.out.println("Current Record is: "+r.toString());
//      System.out.println(r.video().toString());
//      System.out.println(s.get(r.video()).toString());
      assertNotNull(s.get(r.video()));

    }
    assertEquals(i, 3);
  }
  @Test
  public void testIterator2() {
    class compare_by_numowned implements Comparator<Record>{
      public int compare(Record r1, Record r2){
        return r1.numOwned()-r2.numOwned();
      }
    }
    class compare_by_date implements Comparator<Record>{
      public int compare(Record r1, Record r2){
        return r1.video().year()-r2.video().year();
      }
    }

    compare_by_numowned c1 = new compare_by_numowned();
    compare_by_date c2 = new compare_by_date();

    InventorySet s = new InventorySet();
    s.addNumOwned(va,4);
    s.addNumOwned(vb, 7);
    s.addNumOwned(vc,8);
    Iterator<Record> ir = s.iterator(c1);
    //NOTE: All video objects that i'm using have unique titles so its an appropriate substitute
    //for comparing the full string.
    Record r = ir.next();
    assertEquals(r.video().title(),"A");
    r=ir.next();
    assertEquals(r.video().title(),"B");
    r=ir.next();
    assertEquals(r.video().title(),"C");
    assertFalse(ir.hasNext());

    Iterator<Record> ir2 = s.iterator(c2);
    //NOTE: All video objects that i'm using have unique years so its an appropriate substitute
    //for comparing the full string.
    Record r2 = ir2.next();
    assertEquals(r2.video().year(),1999);
    r2=ir2.next();
    assertEquals(r2.video().year(),2000);
    r2=ir2.next();
    assertEquals(r2.video().year(),2001);
    assertFalse(ir2.hasNext());
  }
}
