package gui.menu;

import util.InputHandler;

public class MenuLogic {
  public void update(InputHandler input, Menu menu) {
    char key = input.getKeyValue();
    switch (key) {
      case InputHandler.KEY_W:
        menu.focusNextButton(); 
        break;
      case InputHandler.KEY_S:
        menu.focusPrevButton(); 
        break;
      case InputHandler.KEY_ENTER:
        menu.activateCurrButton();
        break;
    }
  }
}
