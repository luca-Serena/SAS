package Match;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.ImageIcon;

/**
 *
 * @author utente
 */
public class Card implements Serializable {

    String command;
    ImageIcon image;
    int priority;
    int idOwner;

    public Card(String istr) {
        this.command = istr;
        Random rn = ThreadLocalRandom.current();
        switch (istr) {
            case "move1":
                this.image = new ImageIcon(new ImageIcon("tiles/card-move1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 490 + Math.abs ( rn.nextInt() % 170);
                break;
            case "move2":
                this.image = new ImageIcon(new ImageIcon("tiles/card-move2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 670 + Math.abs (rn.nextInt() % 110);
                break;
            case "turnL":
                this.image = new ImageIcon(new ImageIcon("tiles/card-turnL.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 70 + Math.abs (rn.nextInt() % 340);
                break;
            case "turnR":
                this.image = new ImageIcon(new ImageIcon("tiles/card-turnR.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 80 + Math.abs (rn.nextInt() % 340);
                break;
            case "move3":
                this.image = new ImageIcon(new ImageIcon("tiles/card-move3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 790 + Math.abs (rn.nextInt() % 50);
                break;
            case "Uturn":
                this.image = new ImageIcon(new ImageIcon("tiles/card-uturn.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 10 + Math.abs (rn.nextInt() % 50);
                break;
            case "backup":
                this.image = new ImageIcon(new ImageIcon("tiles/card-backup.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                this.priority = 430 + Math.abs (rn.nextInt() % 50);
                break;
            default: System.err.println (istr + " non esistente");
        }
    }

    public String getCommand() {
        return command;
    }

    public ImageIcon getImage() {
        return image;
    }

    public int getPriority() {
        return priority;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    
    
   static Card[] getRandomPool(int size) {
        Card[] pool = new Card[size];
        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) {
            int res = Math.abs(random.nextInt() % 14);
            switch (res) {
                case 0:
                    pool[i] = new Card("Uturn");
                    break;
           
                case 1:case 2:case 3:
                    pool[i] = new Card("turnL");
                    break;               
                case 4: case 5: case 6:
                    pool[i] = new Card("turnR");
                    break;
                case 7:
                    pool[i] = new Card("Uturn");
                    break;
                  
                case 8: case 9: case 10:
                    pool[i] = new Card("move1");
                    break;
                case 11: case 12:
                    pool[i] = new Card("move2");
                    break;
                case 13:
                    pool[i] = new Card("move3");
                    break;
                default:
                    System.out.println(res);
            }
        }
        return pool;
    }
    public static ArrayList <Card> getSortedList(ArrayList <Card> cards) {
         for(int i=1; i<cards.size(); i++) {
         int temp;
         if(cards.get(i-1).priority < cards.get(i).priority) {
            temp = cards.get(i-1).priority;
            cards.get(i-1).priority = cards.get(i).priority;
            cards.get(i).priority = temp;
         }
      }
        return cards;
    }
}
