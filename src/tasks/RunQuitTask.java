package tasks;

import java.lang.Runnable;
import gui.Button;

public class RunQuitTask implements Runnable {
  private Button caller;

  public RunQuitTask(Button caller) {
    this.caller = caller;
  }
  
  @Override
  public void run() {
    caller.setPressed(true);
  }
}
