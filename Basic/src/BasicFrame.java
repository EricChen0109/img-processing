import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BasicFrame extends JFrame {
	BufferedImage oldImg;
	JPanel controlPanel;
	ImagePanel imagePanel;
	JButton btnShow;
	JButton btnInverse;
	JButton btnGray;
	JButton btnUpDown;
	JButton btnLeftRight;
	JButton btnRotatetRight;
	JButton btnRotatetLeft;
	JButton btnRotate180;
	JButton btnSave;
	JLabel lbFile;
	JTextField tfFile;
	int[][][] data;
	int height;
	int width;
	BufferedImage img = null;
	BufferedImage img1 = null;
	boolean sizeChanged;

	BasicFrame() {
		setTitle("影像處理 Demo 1");

		btnShow = new JButton("顯示");
		btnInverse = new JButton("反白 (負片效果)");
		btnGray = new JButton("灰階 (黑白片效果)");
		btnUpDown = new JButton("上下顚倒");
		btnLeftRight = new JButton("左右相反");
		btnRotatetRight = new JButton("向右90度");
		btnRotatetLeft = new JButton("向左90度");
		btnRotate180 = new JButton("旋轉180度");
		btnSave = new JButton("Save PNG ");
		tfFile = new JTextField(10);
		lbFile = new JLabel("Name: ");
		tfFile.setText("Image_");
		controlPanel = new JPanel();
		controlPanel.add(btnShow);
		controlPanel.add(btnInverse);
		controlPanel.add(btnGray);
		controlPanel.add(btnUpDown);
		controlPanel.add(btnLeftRight);
		controlPanel.add(btnRotatetRight);
		controlPanel.add(btnRotatetLeft);
		controlPanel.add(btnRotate180);
		controlPanel.add(btnSave);
		controlPanel.add(lbFile);
		controlPanel.add(tfFile);
		setLayout(new BorderLayout());
		add(controlPanel, BorderLayout.PAGE_START);
		imagePanel = new ImagePanel();
		add(imagePanel);

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					 try {
						img = ImageIO.read(new File("file/F16.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				height = img.getHeight();
				width = img.getWidth();
				data = new int[height][width][3];
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int rgb = img.getRGB(x, y);
						data[y][x][0] = Util.getR(rgb);
						data[y][x][1] = Util.getG(rgb);
						data[y][x][2] = Util.getB(rgb);
					}
			}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
		}});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File outputfile = new File(tfFile.getText() + ".png");
				try {
					if (sizeChanged)
						ImageIO.write(img1, "png", outputfile);
					else
						ImageIO.write(img, "png", outputfile);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		btnInverse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = false;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int r = 255 - data[y][x][0];
						int g = 255 - data[y][x][1];
						int b = 255 - data[y][x][2];
						img.setRGB(x, y, Util.makeColor(r, g, b));
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});
		
		btnGray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = false;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						double r1 = data[y][x][0]*0.299;
						double g1 = data[y][x][1]*0.587;
						double b1 = data[y][x][2]*0.114;
						int r = (int)Math.round(r1);
						int g = (int)Math.round(g1);
						int b = (int)Math.round(b1);
						int gray = r+g+b;
						img.setRGB(x, y, Util.makeColor(gray, gray, gray));
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});

		btnUpDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = false;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) { 
						int rgb = Util.makeColor(data[height -1- y][x][0], 
				                                 data[height -1- y][x][1],
				                                 data[height -1- y][x][2]);
						img.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});

		btnLeftRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = false;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) { 
						int rgb = Util.makeColor(data[y][width-1-x][0], 
				                                 data[y][width-1-x][1],
				                                 data[y][width-1-x][2]);
						img.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});

		btnRotatetRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = true;
				img1 = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < width; y++) {
					for (int x = 0; x < height; x++) {
						int rgb = Util.makeColor(data[height - x - 1][y][0], 
								                 data[height - x - 1][y][1],
								                 data[height - x - 1][y][2]);
						img1.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img1, 0, 0, null);
			}
		});

		btnRotatetLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = true;
				img1 = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < width; y++) {
					for (int x = 0; x < height; x++) {
						int rgb = Util.makeColor(data[x][width-1-y][0], 
								                 data[x][width-1-y][1],
								                 data[x][width-1-y][2]);
						img1.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img1, 0, 0, null);
			}
		});

		btnRotate180.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sizeChanged = false;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) { 
						int rgb = Util.makeColor(data[height -1- y][width-1-x][0], 
				                                 data[height -1- y][width-1-x][1],
				                                 data[height -1- y][width-1-x][2]);
						img.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});
	}

	public static void main(String[] args) {
		BasicFrame frame = new BasicFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
