package gui.help;

import util.InputHandler;

public class HelpLogic {
  public void update(InputHandler input, Help help) {
    char key = input.getKeyValue();
    switch (key) {
      case InputHandler.KEY_ENTER:
        help.activateCurrButton();
        break;
    }
  }
}
