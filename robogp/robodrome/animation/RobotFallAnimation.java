package robogp.robodrome.animation;

/**
 *
 * @author claudia
 */
public class RobotFallAnimation extends Animation {
  public static float DEGREES = 1080;
  private int startSize;
  
  private RobotFallAnimation() {
    this.type = Animation.Type.ROBOT_FALL;
  }
  
  public RobotFallAnimation(String robotName) {
      this();
      which = robotName;
      duration = Animation.TIMEUNIT;
  }

    /**
     * @return the startSize
     */
    public int getStartSize() {
        return startSize;
    }

    /**
     * @param startSize the startSize to set
     */
    public void setStartSize(int startSize) {
        this.startSize = startSize;
    }
}
