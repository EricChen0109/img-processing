import java.awt.Graphics;
import javax.swing.JPanel;

public class ImagePanelLeft extends JPanel{
	//@@ override paintComponent(Graphics g)
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		if (Util.imgLeft != null) {
			g.drawImage(Util.imgLeft, 0, 0, null);
	}
	}
}
