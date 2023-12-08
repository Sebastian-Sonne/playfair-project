import javax.swing.JFrame;

/**
 * Class to setup the frame of a graphical output
 * 
 * @author Sebastian Sonne
 * @version v2 | 25.06.23
 */
public class Frame extends JFrame{
    
    /**
     * constructor of Frame Class
     */
    public Frame()
    {   
        this.add(new Panel());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setFocusable(false);
        this.setVisible(true);
        this.setTitle("Verschlüsselungsmaschine");
    }
}
