package robogp.matchmanager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robogp.robodrome.Direction;
import robogp.robodrome.image.ImageUtil;

/**
 *
 * @author claudia
 */
public class RobotMarker implements Serializable {

    private transient BufferedImage robotImage;
    private final String name;
    private final String color;
    private String owner;
    private int dockNumber;
    private Direction dir;
    private int X;
    private int Y;
    private int nCheckPoint;

    public RobotMarker(String name, String color) {
        this.name = name;
        this.color = color;
        this.dockNumber = 0;
        this.dir = Direction.E;
        this.nCheckPoint=0;
    }

    public BufferedImage getImage(int size) {
        if (this.robotImage == null) {
            String imgFile = "robots/" + name + ".png";
            try {
                robotImage = ImageIO.read(new File(imgFile));
            } catch (IOException ex) {
                Logger.getLogger(RobotMarker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ImageUtil.scale(ImageUtil.superImpose(null, this.robotImage),size, size);
    }

    public void assign(String nickname, int dock) {
        this.owner = nickname;
        this.dockNumber = dock;
    }

    public void free() {
        this.owner = null;
        this.dockNumber = 0;
    }

    public String getColor() {
        return color;
    }

    public int getnCheckPoint() {
        return nCheckPoint;
    }

    public void setnCheckPoint(int nCheckPoint) {
        this.nCheckPoint = nCheckPoint;
    }

    
    
    public boolean isAssigned() {
        return (this.dockNumber > 0);
    }

    public String getOwner() {
        return this.owner;
    }

    public int getDock() {
        return this.dockNumber;
    }
    
    public String getName() {
        return name;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }
    
}
