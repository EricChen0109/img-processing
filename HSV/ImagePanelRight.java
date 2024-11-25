import java.awt.Graphics;

import javax.swing.JPanel;

public class ImagePanelRight extends JPanel{
	//@@ override paintComponent(Graphics g)
	public void paintComponent(Graphics g) { 
		if (Util.imgRight != null) {
			g.drawImage(Util.imgRight, 0, 0, null);
		}
	}
}
