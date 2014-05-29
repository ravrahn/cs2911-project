import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image menuBackground;
	public MenuPanel() {
		super();
	    try {
	    	menuBackground = ImageIO.read(new File("menu-background.png"));
	    } catch (Exception eeeeeeeeee) {
	    	menuBackground = null;
	    }
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
        g.drawImage(menuBackground, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}