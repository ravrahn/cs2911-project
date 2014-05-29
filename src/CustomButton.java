import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * @author Gabriel
 *
 */
public class CustomButton extends JButton {
	public CustomButton() {
		setBorder(new LineBorder(new Color(63, 63, 63), 4));
		setForeground(new Color(119, 136, 153));
		setBackground(new Color(255, 255, 255));
		setFocusable(false);
	}
	
	private static final long serialVersionUID = 1L;
}
