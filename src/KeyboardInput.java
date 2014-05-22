import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A keyboard input processor
 * 
 * @author Gabriel
 */
public class KeyboardInput implements KeyListener {
	public KeyboardInput() {
		currentKeys = new boolean[KEY_COUNT];
		keys = new KeyState[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; i++) {
			keys[i] = KeyState.RELEASED;
		}
	}
	
	public synchronized void poll() {
		for (int i = 0; i < KEY_COUNT; i++) {
			if (currentKeys[i]) {
				if (keys[i] == KeyState.RELEASED) {
					keys[i] = KeyState.ONCE;
				} else {
					keys[i] = KeyState.PRESSED;
				}
			} else {
				keys[i] = KeyState.RELEASED;
			}
		}
	}

	public boolean keyDown(int keyCode) {
		if (keyCode < 0 || keyCode >= KEY_COUNT) {
			throw new IllegalArgumentException("Key not stored");
		}
		return keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED;
	}
	
	public boolean keyDownOnce(int keyCode) {
		if (keyCode < 0 || keyCode >= KEY_COUNT) {
			throw new IllegalArgumentException("Key not stored");
		}
		return keys[keyCode] == KeyState.ONCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KEY_COUNT) {
			currentKeys[keyCode] = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KEY_COUNT) {
			currentKeys[keyCode] = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// Chill
	}
	
	private enum KeyState {RELEASED, PRESSED, ONCE};
	private boolean[] currentKeys;
	private KeyState[] keys;
	private static final int KEY_COUNT = 256;
}
