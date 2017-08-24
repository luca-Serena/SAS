package Match;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author utente
 */
public class Card {

    String command;
    ImageIcon image;
    int priority;

    public Card(String istr) {
        this.command = istr;
        Random rn = new Random();
        switch (istr) {
            case "move1":
                this.image = new ImageIcon(new ImageIcon("tiles/card-move1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 490 + rn.nextInt() % 170;
                break;
            case "move2":
                this.image = new ImageIcon(new ImageIcon("tiles/card-move2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 670 + rn.nextInt() % 110;
                break;
            case "turnL":
                this.image = new ImageIcon(new ImageIcon("tiles/card-turnL.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 70 + rn.nextInt() % 340;
                break;
            case "turnR":
                this.image = new ImageIcon(new ImageIcon("tiles/card-turnR.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 80 + rn.nextInt() % 340;
                break;
            case "move3":
                this.image = new ImageIcon(new ImageIcon("tiles/card-move3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 790 + rn.nextInt() % 50;
                break;
            case "Uturn":
                this.image = new ImageIcon(new ImageIcon("tiles/card-uturn.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 10 + rn.nextInt() % 50;
                break;
            case "backup":
                this.image = new ImageIcon(new ImageIcon("tiles/card-backup.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 430 + rn.nextInt() % 50;
                break;
        }
    }

    static Card[] getRandomPool(int size) {
        Card[] pool = new Card[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int res = random.nextInt() % 14;
            switch (res) {
                case 0:
                    pool[i] = new Card("Uturn");
                    break;
                case 1:
                case 2:
                case 3:
                    pool[i] = new Card("turnL");
                    break;
                case 4:
                case 5:
                case 6:
                    pool[i] = new Card("turnR");
                    break;
                case 7:
                    pool[i] = new Card("Uturn");
                    break;
                case 8:
                case 9:
                case 10:
                    pool[i] = new Card("move1");
                    break;
                case 11:
                case 12:
                    pool[i] = new Card("move2");
                    break;
                case 13:
                    pool[i] = new Card("move3");
                    break;
            }
        }
        return pool;
    }
}
