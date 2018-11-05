package shop.main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import shop.command.Command;
import shop.command.RerunnableCommand;
import shop.command.UndoableCommand;
import shop.data.Data;
import shop.data.Record;
import shop.data.Video;
import shop.data.Inventory;
import java.util.Iterator;


// write an integration test that tests the data classes.
// add in some videos, check out, check in, delete videos, etc.
// check that errors are reported when necessary.
// check that things are going as expected.
public class TEST1 {
  private Inventory _inventory = Data.newInventory();
  
  @Test
  private void check(Video v, int numOwned, int numOut, int numRentals) {
    Record r = _inventory.get(v);
    assertEquals(numOwned, r.numOwned());
    assertEquals(numOut, r.numOut());
    assertEquals(numRentals, r.numRentals());
  }
  @Test  
  public void test1() {
    Command clearCmd = Data.newClearCmd(_inventory);
    clearCmd.run();

    Video v1 = Data.newVideo("Title1", 2000, "Director1");
    assertEquals(0, _inventory.size());
    assertTrue(Data.newAddCmd(_inventory, v1, 5).run());
    assertEquals(1, _inventory.size());
    assertTrue(Data.newAddCmd(_inventory, v1, 5).run());
    assertEquals(1, _inventory.size());
    // System.out.println(_inventory.get(v1));
    check(v1,10,0,0);
    Video v2 = Data.newVideo("Title2", 2001, "Director2");
    assertTrue(Data.newAddCmd(_inventory,v2,7).run());
    assertTrue(Data.newOutCmd(_inventory,v2).run());
    check(v2,7,1,1);
  }

  @Test
  public void testadd() {
    Command clearCmd = Data.newClearCmd(_inventory);
    Command undo = Data.newUndoCmd(_inventory);
    Command redo = Data.newRedoCmd(_inventory);
    assertTrue(clearCmd.run());
    assertFalse(clearCmd.run());
    Video v1 = Data.newVideo("Title1", 2000, "Director1");
    Video v2 = Data.newVideo("  Title2   ", 2001, "  Director2   ");
    Video v3 = Data.newVideo("Title3", 2002, "Director3");
    Video v4 = Data.newVideo("Title4", 2003, "Director4");
    //Testing that normal initial adds work
    assertTrue(Data.newAddCmd(_inventory, v1, 5).run());
    assertEquals(_inventory.size(),1);
    undo.run();
    //assertFalse(undo.run());
    assertEquals(_inventory.size(),0);
    redo.run();
    assertFalse(redo.run());
    assertEquals(_inventory.size(),1);
    check(v1,5,0,0);
    assertTrue(Data.newAddCmd(_inventory, v2, 7).run());
    assertTrue(Data.newAddCmd(_inventory, v4, 15).run());
    //Testing that bad adds don't work
    assertFalse(Data.newAddCmd(_inventory, null, 6).run());
    assertFalse(Data.newAddCmd(_inventory, v2, -8).run());
    assertFalse(Data.newAddCmd(_inventory, v3, 0).run());
    Command outcmd = Data.newOutCmd(_inventory,v4);
    assertTrue(outcmd.run());
    assertFalse(outcmd.run());
    assertFalse(Data.newAddCmd(_inventory,v4,-15).run());

    //Testing if add and subtracting to prexisting works
    assertTrue(Data.newAddCmd(_inventory, v2, -3).run());
    assertTrue(Data.newAddCmd(_inventory, v1, 6).run());
    check(v1,11,0,0);
    check(v2,4,0,0);

    //Testing removing a video if total is zero
    assertTrue(Data.newAddCmd(_inventory, v3, 6).run());
    assertTrue(Data.newAddCmd(_inventory, v3, -6).run());
    assertNull(_inventory.get(v3));
  }

  @Test
  public void testclear(){
    Command clearcmd = Data.newClearCmd(_inventory);
    RerunnableCommand  undo= Data.newUndoCmd(_inventory);
    RerunnableCommand redo = Data.newRedoCmd(_inventory);
    clearcmd.run();
    assertEquals(_inventory.size(),0);

    Video v1 = Data.newVideo("Title1", 2000, "Director1");
    Video v2 = Data.newVideo("  Title2   ", 2001, "  Director2   ");
    Video v3 = Data.newVideo("Title3", 2002, "Director3");

    assertTrue(Data.newAddCmd(_inventory, v1, 5).run());
    assertTrue(Data.newAddCmd(_inventory, v2, 7).run());
    assertTrue(Data.newAddCmd(_inventory, v3, 15).run());

    Data.newClearCmd(_inventory).run();
    assertEquals(_inventory.size(),0);
    undo.run();
    assertEquals(_inventory.size(),3);
    check(v1,5,0,0);
    redo.run();
    assertEquals(_inventory.size(),0);

  }

  @Test
  public void testInandOut(){
    //Clearing the list and doing some precursery checks.
    Command clearcmd = Data.newClearCmd(_inventory);
    Command undo = Data.newUndoCmd(_inventory);
    Command redo = Data.newRedoCmd(_inventory);
    clearcmd.run();
    assertEquals(_inventory.size(),0);

    Video v1 = Data.newVideo("Title1", 2000, "Director1");
    Video v2 = Data.newVideo("  Title2   ", 2001, "  Director2   ");
    Video v3 = Data.newVideo("Title3", 2002, "Director3");
    //Initializing in and out commands and adding a video to the inventory.
    assertTrue(Data.newAddCmd(_inventory,v1,3).run());

    //Checking that out works.
    assertTrue(Data.newOutCmd(_inventory,v1).run());
    assertTrue(Data.newOutCmd(_inventory,v1).run());
    check(v1,3,2,2);
    undo.run();
    check(v1,3,1,1);
    redo.run();
    check(v1,3,2,2);
    assertTrue(Data.newOutCmd(_inventory,v1).run());
    assertFalse(Data.newOutCmd(_inventory,v1).run());
    assertFalse(Data.newOutCmd(_inventory,v2).run());

    check(v1,3,3,3);

    //Checking that out works correctly.
    assertTrue(Data.newInCmd(_inventory,v1).run());
    assertTrue(Data.newInCmd(_inventory,v1).run());
    check(v1,3,1,3);
    undo.run();
    check(v1,3,2,3);
    redo.run();
    check(v1,3,1,3);
    assertTrue(Data.newInCmd(_inventory,v1).run());
    assertFalse(Data.newInCmd(_inventory,v1).run());

    assertFalse(Data.newInCmd(_inventory,v2).run());

    check(v1,3,0,3);



  }

}
