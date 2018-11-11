package shop.ui;


public interface UIMenu {
  int size();
  String getHeading();
  String getPrompt(int i);
  void runAction(int i);
}
