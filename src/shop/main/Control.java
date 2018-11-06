package shop.main;

import shop.command.Command;
import shop.data.Data;
import shop.data.Inventory;
import shop.data.Record;
import shop.data.Video;
import shop.ui.*;

import java.util.Iterator;


class Control {

  enum States{
    EXITED(null), EXIT(MainMenu.values()), START(MainMenu.values());

    private final MainMenu[] options;

    States(MainMenu[] options){
      this.options=options;
    }

    private MainMenu[] getOptions(){
      return options;
    }

  }
  enum Menus{
    MAINMENU, EXITMENU
  }
  enum MainMenu{
    DEFAULT("Default", new UIMenuAction() {
      public void run() {
        _ui.displayError("doh!");
      }}),
    UPDATE("Add/Remove copies of a video",new UIMenuAction() {
      public void run() {
        String[] result1 = _ui.processForm(_getVideoForm);
        Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

        UIFormBuilder f = new UIFormBuilder();
        f.add("Number of copies to add/remove", _numberTest);
        String[] result2 = _ui.processForm(f.toUIForm(""));

        Command c = Data.newAddCmd(_inventory, v, Integer.parseInt(result2[0]));
        if (! c.run()) {
          _ui.displayError("Command failed");
        }
      }
    }),
    CHECKIN("Check in a video",
                  new UIMenuAction() {
      public void run() {
        String[] result1 = _ui.processForm(_getVideoForm);
        Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

        Command c = Data.newInCmd(_inventory,v);
        if(! c.run()){
          _ui.displayError("Command failed");
        }
      }
    }),
    CHECKOUT("Check out a video",
            new UIMenuAction() {
      public void run() {
        String[] result1 = _ui.processForm(_getVideoForm);
        Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

        Command c = Data.newOutCmd(_inventory,v);
        if(! c.run()){
          _ui.displayError("Command failed");
        }
      }
    }),
    PRINT("Print the inventory",
            new UIMenuAction() {
      public void run() {
        _ui.displayMessage(_inventory.toString());
      }
    }),
    CLEAR("Clear the inventory",
            new UIMenuAction() {
      public void run() {
        if (!Data.newClearCmd(_inventory).run()) {
          _ui.displayError("Command failed");
        }
      }
    }),
    UNDO("Undo",
            new UIMenuAction() {
      public void run() {
        if (!Data.newUndoCmd(_inventory).run()) {
          _ui.displayError("Command failed");
        }
      }
    }),
    REDO("Redo",
            new UIMenuAction() {
      public void run() {
        if (!Data.newRedoCmd(_inventory).run()) {
          _ui.displayError("Command failed");
        }
      }
    }),
    TOP10("Print top ten all time rentals in order",
            new UIMenuAction() {
      public void run() {
        Record[] rs = new Record[10];
        int i =0;
        Iterator<Record> ir =_inventory.iterator((Record x,Record y) -> y.numOwned()-x.numOwned());
        StringBuilder sb = new StringBuilder();
        while(ir.hasNext() && i<10){
          sb.append(ir.next().toString());
          sb.append("\n");
          //_ui.displayMessage(ir.next().toString());
          i++;
        }
        _ui.displayMessage(sb.toString());
      }
    }),
    EXIT("Exit",
            new UIMenuAction() {
      public void run() {
        _state = States.EXIT;
      }
    }),
    INIT("Initialize with bogus contents",
            new UIMenuAction() {
      public void run() {
        Data.newAddCmd(_inventory, Data.newVideo("a", 2000, "m"), 1).run();
        Data.newAddCmd(_inventory, Data.newVideo("b", 2000, "m"), 2).run();
        Data.newAddCmd(_inventory, Data.newVideo("c", 2000, "m"), 3).run();
        Data.newAddCmd(_inventory, Data.newVideo("d", 2000, "m"), 4).run();
        Data.newAddCmd(_inventory, Data.newVideo("e", 2000, "m"), 5).run();
        Data.newAddCmd(_inventory, Data.newVideo("f", 2000, "m"), 6).run();
        Data.newAddCmd(_inventory, Data.newVideo("g", 2000, "m"), 7).run();
        Data.newAddCmd(_inventory, Data.newVideo("h", 2000, "m"), 8).run();
        Data.newAddCmd(_inventory, Data.newVideo("i", 2000, "m"), 9).run();
        Data.newAddCmd(_inventory, Data.newVideo("j", 2000, "m"), 10).run();
        Data.newAddCmd(_inventory, Data.newVideo("k", 2000, "m"), 11).run();
        Data.newAddCmd(_inventory, Data.newVideo("l", 2000, "m"), 12).run();
        Data.newAddCmd(_inventory, Data.newVideo("m", 2000, "m"), 13).run();
        Data.newAddCmd(_inventory, Data.newVideo("n", 2000, "m"), 14).run();
        Data.newAddCmd(_inventory, Data.newVideo("o", 2000, "m"), 15).run();
        Data.newAddCmd(_inventory, Data.newVideo("p", 2000, "m"), 16).run();
        Data.newAddCmd(_inventory, Data.newVideo("q", 2000, "m"), 17).run();
        Data.newAddCmd(_inventory, Data.newVideo("r", 2000, "m"), 18).run();
        Data.newAddCmd(_inventory, Data.newVideo("s", 2000, "m"), 19).run();
        Data.newAddCmd(_inventory, Data.newVideo("t", 2000, "m"), 20).run();
        Data.newAddCmd(_inventory, Data.newVideo("u", 2000, "m"), 21).run();
        Data.newAddCmd(_inventory, Data.newVideo("v", 2000, "m"), 22).run();
        Data.newAddCmd(_inventory, Data.newVideo("w", 2000, "m"), 23).run();
        Data.newAddCmd(_inventory, Data.newVideo("x", 2000, "m"), 24).run();
        Data.newAddCmd(_inventory, Data.newVideo("y", 2000, "m"), 25).run();
        Data.newAddCmd(_inventory, Data.newVideo("z", 2000, "m"), 26).run();
      }
    });
    private final String prompt;
    private final UIMenuAction action;

    MainMenu(String prompt, UIMenuAction action){
      this.prompt =prompt;
      this.action = action;
    }

    private String getPrompt(){
      return prompt;
    }

    private UIMenuAction getAction(){
      return action;
    }

    private final static String header="Bob's Video";

  }
  private static final int NUMSTATES = 10;
  private UIMenu[] _menus;
  private static States _state;

  private static UIForm _getVideoForm;
  private static UIFormTest _numberTest;
  private static UIFormTest _stringTest;
    
  private static Inventory _inventory;
  private static UI _ui;



  Control(Inventory inventory, UI ui) {
    _inventory = inventory;
    _ui = ui;

    _menus = new UIMenu[NUMSTATES];
    _state = States.START;

    //addEXIT(States.EXIT);
    
    UIFormTest yearTest = new UIFormTest() {
        public boolean run(String input) {
          try {
            int i = Integer.parseInt(input);
            return i > 1800 && i < 5000;
          } catch (NumberFormatException e) {
            return false;
          }
        }
      };
    _numberTest = new UIFormTest() {
        public boolean run(String input) {
          try {
            Integer.parseInt(input);
            return true;
          } catch (NumberFormatException e) {
            return false;
          }
        }
      };
    _stringTest = new UIFormTest() {
        public boolean run(String input) {
          return ! "".equals(input.trim());
        }
      };

    UIFormBuilder f = new UIFormBuilder();
    f.add("Title", _stringTest);
    f.add("Year", yearTest);
    f.add("Director", _stringTest);
    _getVideoForm = f.toUIForm("Enter Video");
  }
  
  void run() {
    try {
      while (_state != States.EXITED) {
        //_ui.processMenu(_state.getOptions());
      }
    } catch (UIError e) {
      _ui.displayError("UI closed");
    }
  }
  

  private void addEXIT(int stateNum) {
    UIMenuBuilder m = new UIMenuBuilder();
    
    m.add("Default", new UIMenuAction() { public void run() {} });
    m.add("Yes",
      new UIMenuAction() {
        public void run() {
          _state = States.EXITED;
        }
      });
    m.add("No",
      new UIMenuAction() {
        public void run() {
          _state = States.START;
        }
      });
    
    _menus[stateNum] = m.toUIMenu("Are you sure you want to exit?");
  }
}
