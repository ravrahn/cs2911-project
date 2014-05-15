

import java.awt.Graphics;
import java.util.Timer;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author ntpmtv
 *
 */
public class MazeBoard extends JPanel{


	//private Timer timer;
	private mComponent map;
	
	public MazeBoard() {
		map = new mComponent();
		//timer = new Timer();
		
	}
	
	public void paint(Graphics graphic){
		//Menu menu = new Menu();
		
		for (int y = 0; y < 24; y++) {
			for (int x = 0; x < 24; x++) {
				if(map.getLayout(x, y).equals("g") ){
					graphic.drawImage(map.getField(), x*23,y*23,30,30,null);
				}
				if(map.getLayout(x, y).equals("w") ){
					graphic.drawImage(map.getWall(), x*23,y*23,30,30,null);
				}
			}
		}
		
		
	} 
	
	 

}
