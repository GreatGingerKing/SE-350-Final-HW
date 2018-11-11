package shop.ui;

public class UIFactory {
  private UIFactory() {}

  static public UI getPopupUI () {
    return new PopupUI();
  }
  static public UI getTextUI(){
    return new TextUI();
  }
}
