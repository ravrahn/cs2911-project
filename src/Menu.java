
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class Menu extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setMinimumSize(new Dimension(800, 600));
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new MenuPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JLabel titleLabel = new JLabel("Maze");
		titleLabel.setBorder(new EmptyBorder(0, 28, 0, 28));
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Roboto Slab", Font.PLAIN, 96));
		contentPane.add(titleLabel);
		
		contentPane.add(Box.createHorizontalGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(128, contentPane.getHeight()));
		panel.setOpaque(false);
		contentPane.add(panel);

		panel.add(Box.createVerticalGlue());
		
		JButton playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(128, 64));
		playButton.setBackground(new Color(0xededed));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("play");
				//TODO call Function Play
			}
		});
		playButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 28));
		playButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.add(playButton);
		
		panel.add(Box.createVerticalGlue());
		
		JButton helpButton = new JButton("Help");
		helpButton.setPreferredSize(new Dimension(128, 64));
		helpButton.setMinimumSize(new Dimension(128, 64));
		helpButton.setBackground(new Color(0xededed));
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Help");
				//TODO Function Help and Hint
			}
		});
		helpButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 28));
		panel.add(helpButton);
		
		panel.add(Box.createVerticalGlue());
		
		JButton quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(128, 64));
		quitButton.setBackground(new Color(0xededed));
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quit");
				System.exit(0);
			}
		});
		quitButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 28));
		panel.add(quitButton);
		
		panel.add(Box.createVerticalGlue());
		

		contentPane.add(Box.createHorizontalGlue());
		
	}

}
