package tasks;

import java.lang.Runnable;
import gui.Button;
import gui.help.Help;

public class RunHelpTask implements Runnable {
  private Button caller;

  public RunHelpTask(Button caller) {
    this.caller = caller;
  }

  @Override
  public void run() {
    Help help = new Help();  
    help.run();
  }
}
