package shop.data;

import shop.command.UndoableCommand;

/**
 * Implementation of command to check out a video.
 * @see Data
 */
final class CmdOut implements UndoableCommand {
  private boolean _runOnce;
  private InventorySet _inventory;
  private Record _oldvalue;
  private Video _video;
  CmdOut(InventorySet inventory, Video video) {
    _inventory = inventory;
    _video = video;
  }
  public boolean run() {
    if (_runOnce) {
      return false;
    }
    _runOnce = true;
    //System.out.println(_inventory.get(_video) + " " + _video);
    try {
      _oldvalue = _inventory.checkOut(_video);
      _inventory.getHistory().add(this);
      //System.out.println("ok");
      return true;
    } catch (IllegalArgumentException e) {
      //System.out.println("IAE");
      return false;
    } catch (ClassCastException e) {
      //System.out.println("CCE");
      return false;
    }
  }
  public void undo() {
    _inventory.replaceEntry(_video,_oldvalue);
  }
  public void redo() {
    _inventory.checkOut(_video);
  }
}
