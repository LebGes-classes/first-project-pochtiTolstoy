package gfx;

public class FPS {
  private static final long milisecBase = 1_000_000;
  private static final long nanosecBase = 1_000_000_000L;

  private final int fps;
  private long nanosecPerFrame;
  private long lastTime;
  private long currTime;
  private long delta;

  public FPS(int fps) {
    this.fps = fps;
    deduceNanosecFromFPS();
    this.lastTime = System.nanoTime();
  }

  public void lock() {
    if (delta >= nanosecPerFrame) return;
    long sleepTime = (nanosecPerFrame - delta) / milisecBase;
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void update() {
    currTime = System.nanoTime();
    delta = currTime - lastTime;
  }

  public boolean free() {
    if (delta >= nanosecPerFrame) {
      lastTime = currTime;
      return true;
    } 
    return false;
  }

  private void deduceNanosecFromFPS() {
    this.nanosecPerFrame = nanosecBase / this.fps;
  }
}
