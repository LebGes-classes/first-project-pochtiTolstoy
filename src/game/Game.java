package game;

import java.io.*;

import gfx.FPS;
import gfx.RendererGame;
import util.InputHandler;
import level.Level;
import gui.UI.UI;

public class Game {
  private InputHandler inputHandler;
  private RendererGame rendererGame;
  private UI ui;
  private GameLogic gameLogic;
  private Level level;
  private FPS fps;
  private PrintWriter out;

  public Game(Level level) {
    this.out = new PrintWriter(System.out);
    this.inputHandler = new InputHandler();
    this.rendererGame = new RendererGame(level, out);
    this.ui = new UI(level, out);
    this.gameLogic = new GameLogic();
    this.level = level;
    this.fps = new FPS(RendererGame.FPS);
  }

  public void processInput() { inputHandler.processInput(); }

  public void update() { 
    gameLogic.update(inputHandler, level); 
    ui.update();
  }

  public void render() { 
    rendererGame.render(); 
    ui.render();
    out.flush();
  }

  public void run() {
    while (isRunnable()) {
      fps.update();
      if (fps.free()) runGameLoop();
      fps.lock(); 
    }
    destroy();
  }

  private void runGameLoop() {
    render();
    processInput();
    update();
  }

  public boolean isRunnable() {
    return !inputHandler.pressedQuit() 
        && !level.isLevelComplete() 
        && level.isPlayerAlive()
        ;
  }

  private void destroy() {
    inputHandler.destroy();
    rendererGame.destroy();
  }
}
