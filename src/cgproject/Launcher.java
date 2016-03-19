package cgproject;

import javax.swing.UIManager;

public class Launcher {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new AppWindow();
	}
	
}
