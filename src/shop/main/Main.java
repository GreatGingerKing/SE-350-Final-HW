package shop.main;

import shop.ui.UIFactory;
import shop.ui.UI;
import shop.data.Data;

public class Main {
  private Main() {}
  public static void main(String[] args) {
    UI ui;
    if (Math.random() <= 0.5) {
      //ui = new shop.ui.TextUI();
      ui = new shop.ui.PopupUI();
    } else {
      ui = new shop.ui.PopupUI();
      //ui = new shop.ui.TextUI();
    }
    Control control = new Control(Data.newInventory(), ui);
    control.run();
  }
}
