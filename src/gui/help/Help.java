package gui.help;

import gfx.FPS;
import gui.help.HelpLogic;
import util.InputHandler;
import gui.Button;
import gui.Button.ButtonType;
import gfx.RendererHelp;
import tasks.*;

public class Help {
  private Button quitButton;
  private InputHandler inputHandler;
  private HelpLogic helpLogic;
  private FPS fps;
  private RendererHelp renderer;

  public Help() {
    this.inputHandler = new InputHandler();
    this.fps = new FPS(RendererHelp.FPS);
    this.helpLogic = new HelpLogic();
    this.quitButton = new Button(79, 45, ButtonType.OK);
    this.renderer = new RendererHelp(quitButton);

    setTasksForButtons();
    quitButton.focus();
  }

  public void run() {
    while (isRunnable()) {
      fps.update();
      if (fps.free()) runHelpLoop();
      fps.lock();
    }
    destroy();
  }
  
  private void runHelpLoop() {
    render();
    processInput();
    update();
  }

  private void render() { renderer.render(); }

  private void processInput() { inputHandler.processInput(); }

  private void update() { helpLogic.update(inputHandler, this); }

  public boolean isRunnable() { return !quitButton.isPressed(); }

  private void destroy() {
    inputHandler.destroy();
    renderer.destroy();
  }

  private void setTasksForButtons() {
    quitButton.setTask(new RunQuitTask(quitButton));
  }

  public void activateCurrButton() {
    quitButton.click();
  }
}
