import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * 
 */

/**
 * @author ntpmtv
 *
 */
public class Player {
	private Image character ;
	/**
	 * 
	 */
	public Player() {
		ImageIcon charc = new ImageIcon("...png");
		character = charc.getImage();
		// TODO Auto-generated constructor stub
	}
	
	public Image getChar(){
		return this.character;
	} 

}
