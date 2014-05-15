import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;





/**
 * 
 */

/**
 * @author ntpmtv
 *
 */
public class mComponent {
	 private Scanner component;  
	// String file = "Layout.txt";
	 private String compo[] = new String[24];
	 private Image field , wall;
	 
	 
	/**
	 * 
	 */
	 	public mComponent() {
			ImageIcon icon = new ImageIcon("Koopa Shell.png");
			field = icon.getImage();
			//System.out.println(field);
			icon = new ImageIcon("Carrot Bonus.png");
			wall = icon.getImage();
			open();
			read();
			close();
	 	}
		
	 	public Image getField(){
	 		return this.field;
	 	}
	 	
		public Image getWall(){
	 		return this.wall;
	 	}
	 	
	 	public String getLayout(int x , int y) {
			String coor = compo[y].substring(x, x+1);
			return coor;
			
		}
	
		public void open(){
			try{
			component = new Scanner(new File("Layout1.txt"));
		
			}
			catch(Exception e){
				System.out.println("Input Component file not found");
			}
		}
		public void read(){
			List<String> tmp = new ArrayList<String>();
			while (component.hasNext()) {
				tmp.add(component.next());
			}
			
			for (int i = 0; i <24; i++) {
				
				compo[i] = tmp.get(i);
				
			}
		}
		public void close(){
			
		}
}
 