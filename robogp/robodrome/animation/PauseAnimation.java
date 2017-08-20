package robogp.robodrome.animation;

/**
 *
 * @author claudia
 */
public class PauseAnimation extends Animation {
    public PauseAnimation(long millisec) {
        this.type = Animation.Type.PAUSE;
        this.which = "";
        this.duration = millisec;
    }

    @Override
    public boolean hasFinished(long elapsed) {
        return (elapsed >= duration);
    }    
}
