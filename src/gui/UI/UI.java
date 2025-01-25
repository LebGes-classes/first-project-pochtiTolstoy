package gui.UI;

import java.io.*;

import gfx.RendererUI;
import gui.UI.UILogic;
import level.Level;

public class UI {
  private RendererUI rendererUI;
  private UILogic uiLogic;

  public UI(Level level, PrintWriter out) {
    this.rendererUI = new RendererUI(level, out);
    this.uiLogic = new UILogic();
  }

  public void render() { rendererUI.render(); }

  public void update() { uiLogic.update(); }
}
