package shop.data;

import shop.command.RerunnableCommand;
import shop.command.UndoableCommand;

import java.util.HashMap;


class Triple<L,M,N>{
  private L l;
  private M m;
  private N n;

  Triple(L l, M m, N n){
    this.l =l;
    this.m = m;
    this.n =n;
  }


  public boolean equals(Object thatObject) {
    if(this==thatObject) return true;
    if(thatObject==null) return false;
    if(!(thatObject instanceof Triple)) return false;

    Triple<L,M,N> that = (Triple<L,M,N>) thatObject;
    if(!(l.equals(that.l))) return false;

    if(! m.equals(that.m)) return false;

    if(!(n.equals(that.n))) return false;

    return true;
  }

  public int hashCode(){
    return l.hashCode()+m.hashCode()+n.hashCode();
  }
}

/**
 * A static class for accessing data objects.
 */
public class Data {
  private Data() {}
  /**
   * Returns a new Inventory.
   */

  static HashMap<Triple<String,Integer,String>,Video> videos = new HashMap<>();
  static public final Inventory newInventory() {
    return new InventorySet();
  }

  /**
   * Factory method for Video objects.
   * Title and director are "trimmed" to remove leading and final space.
   * @throws IllegalArgumentException if Video invariant violated.
   */
  static public Video newVideo(String title, int year, String director) {
    Video v;
    try {
      v = new VideoObj(title, year, director);
    } catch (IllegalArgumentException e) {
      throw e;
    }

    Triple<String,Integer,String > vt = new Triple<>(title.trim(),year,director.trim());

    Video tv = videos.get(vt);

    if(tv==null) {
      videos.put(vt,v);
        return v;
    }
    else{
      return tv;
    }
  }

  /**
   * Returns a command to add or remove copies of a video from the inventory.
   * <p>The returned command has the following behavior:</p>
   * <ul>
   * <li>If a video record is not already present (and change is
   * positive), a record is created.</li>
   * <li>If a record is already present, <code>numOwned</code> is
   * modified using <code>change</code>.</li>
   * <li>If <code>change</code> brings the number of copies to be less
   * than one, the record is removed from the inventory.</li>
   * </ul>
   * @param video the video to be added.
   * @param change the number of copies to add (or remove if negative).
   * @throws IllegalArgumentException if <code>inventory<code> not created by a call to <code>newInventory</code>.
   */
  static public UndoableCommand newAddCmd(Inventory inventory, Video video, int change) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    return new ChangeCmd((InventorySet) inventory, video, change,(I,V,C)->(I.addNumOwned(V,C)));
  }

  /**
   * Returns a command to check out a video.
   * @param video the video to be checked out.
   */
  static public UndoableCommand newOutCmd(Inventory inventory, Video video) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    return new ChangeCmd((InventorySet) inventory, video,0,(I,V,C)->(I.checkOut(V)));
  }
  
  /**
   * Returns a command to check in a video.
   * @param video the video to be checked in.
   */
  static public UndoableCommand newInCmd(Inventory inventory, Video video) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    return new ChangeCmd((InventorySet) inventory, video,0,(I,V,C)->(I.checkIn(V)));
  }
  
  /**
   * Returns a command to remove all records from the inventory.
   */
  static public UndoableCommand newClearCmd(Inventory inventory) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    return new CmdClear((InventorySet) inventory);
  }

  /**
   * Returns a command to undo that will undo the last successful UndoableCommand. 
   */
  static public RerunnableCommand newUndoCmd(Inventory inventory) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    return ((InventorySet)inventory).getHistory().getUndo();
  }

  /**
   * Returns a command to redo that last successfully undone command. 
   */
  static public RerunnableCommand newRedoCmd(Inventory inventory) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    return ((InventorySet)inventory).getHistory().getRedo();
  }
}  
