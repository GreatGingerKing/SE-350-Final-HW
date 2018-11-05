package shop.data;

import shop.command.UndoableCommand;

/**
 * Implementation of command to add or remove inventory.
 * @see Data
 */
final class CmdAdd implements UndoableCommand {
  private boolean _runOnce;
  private InventorySet _inventory;
  private Record _oldvalue;
  private Video _video;
  private int _change;
  CmdAdd(InventorySet inventory, Video video, int change) {
    _inventory = inventory;
    _video = video;
    _change = change;
  }
  public boolean run() {
    if (_runOnce) {
      return false;
    }
    _runOnce = true;
    //System.out.println(_inventory.get(_video) + " " + _video + " " + _change);
    try {
      _oldvalue = _inventory.addNumOwned(_video, _change);
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
    _inventory.addNumOwned(_video, _change);
  }
}
