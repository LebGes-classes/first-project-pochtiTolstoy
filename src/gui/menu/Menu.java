package gui.menu;

import gfx.FPS;
import gfx.RendererMenu;
import gui.menu.MenuLogic;
import util.InputHandler;
import gui.Button;
import gui.Button.ButtonType;
import util.GameMath;
import tasks.*;

public class Menu {
  private Button playButton;
  private Button helpButton;
  private Button quitButton;
  private InputHandler inputHandler;
  private MenuLogic menuLogic;
  private RendererMenu renderer;
  private FPS fps;
  private Button[] buttons;
  private int currButtonID;

  public Menu() {
    this.inputHandler = new InputHandler();
    this.fps = new FPS(RendererMenu.FPS);
    this.menuLogic = new MenuLogic();
    this.playButton = new Button(79, 16, ButtonType.PLAY);
    this.helpButton = new Button(79, 23, ButtonType.HELP);
    this.quitButton = new Button(79, 30, ButtonType.QUIT);
    this.buttons = new Button[]{playButton, helpButton, quitButton};
    this.renderer = new RendererMenu(buttons);
    this.currButtonID = 0;
    setTasksForButtons();
    playButton.focus();
  }

  public void run() {
    while (isRunnable()) {
      fps.update(); 
      if (fps.free()) runMenuLoop();
      fps.lock();
    }
    destroy();
  }

  private void runMenuLoop() {
    render();
    processInput();
    update();
  }

  private void render() { renderer.render(); }

  private void processInput() { inputHandler.processInput(); }

  private void update() { menuLogic.update(inputHandler, this); }

  public boolean isRunnable() { return !quitButton.isPressed(); }

  private void destroy() {
    inputHandler.destroy();
    renderer.destroy();
  }

  public void focusNextButton() {
    buttons[currButtonID].unfocus();
    currButtonID = GameMath.euMod(currButtonID - 1, buttons.length);
    buttons[currButtonID].focus();
  } 

  public void focusPrevButton() {
    buttons[currButtonID].unfocus();
    currButtonID = GameMath.euMod(currButtonID + 1, buttons.length);
    buttons[currButtonID].focus();
  } 

  public void activateCurrButton() {
    buttons[currButtonID].click();
  }

  private void setTasksForButtons() {
    playButton.setTask(new RunGameTask(playButton));
    helpButton.setTask(new RunHelpTask(helpButton));
    quitButton.setTask(new RunQuitTask(quitButton));
  }
}
