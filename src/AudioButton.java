import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;

/**
 * An Audio Playing Component
 * 
 * @author Gabriel
 */
public class AudioButton extends JButton {
	public AudioButton() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		muted = false;
		setText("Mute");
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				muted = !muted;
				if (muted) {
					setText("Unmute");
				} else {
					setText("Mute");
				}
			}
		});
		new PlayThread().start();
	}
	
	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[5];
		public void run() {
			try {
				while (true) {
					audioInputStream = AudioSystem.getAudioInputStream(new File("mountain_king.wav"));
					audioFormat = audioInputStream.getFormat();
					
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					int cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
					while (cnt != -1) {
						if (!muted) {
							sourceDataLine.write(tempBuffer, 0, cnt);
						}
						cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
					}
					
					sourceDataLine.drain();
					sourceDataLine.close();
					System.out.println("Audio Closed");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	AudioFormat audioFormat;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	boolean muted;
	private static final long serialVersionUID = 1L;
}
