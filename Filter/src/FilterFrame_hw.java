
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FilterFrame_hw extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelLP = new JPanel();
	JPanel cotrolPanelHP = new JPanel();
	ImagePanel imagePanel;
	ImagePanel imagePanel2;
	JButton btnShow;
	JButton btnLP = new JButton("Low-Pass(Blur)");
	JButton btnHP = new JButton("High-Pass(Sharp)");
	int[][][] data;
	int[][][] newData;
	int height;
	int width;
	BufferedImage img = null;
	String filename = "Munich.png";

	FilterFrame_hw() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("HW 5: Image Filters 2023/04/06");
		try {
			img = ImageIO.read(new File("Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		btnShow = new JButton("Show");
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);
		cotrolPanelShow.add(btnShow);
		cotrolPanelShow.add(btnLP);
		cotrolPanelShow.add(btnHP);
		cotrolPanelMain.add(cotrolPanelShow);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(20, 220, 700, 700);
		getContentPane().add(imagePanel);
		imagePanel2 = new ImagePanel();
		imagePanel2.setBounds(720, 220, 700, 700);
		getContentPane().add(imagePanel2);

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				img = loadImage(filename);
				height = img.getHeight();
				width = img.getWidth();
				data = new int[height][width][3];
				data = makeRGBData(img);
				drawImg(imagePanel, img);
			}
		});

		btnLP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newData = new int[height][width][3];
				for(int z = 0;z<3;z++) {
					for(int y = 0;y<height;y++) {
						for(int x = 0;x<width;x++) {
							int[][]pix = covert(x,y,z);
							newData[y][x][z] = LP(pix);
						}
					}
				}
				drawImg(imagePanel2,newData);
			}
		});
		
		btnHP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newData = new int[height][width][3];
				for(int z = 0;z<3;z++) {
					for(int y = 0;y<height;y++) {
						for(int x = 0;x<width;x++) {
							int[][]pix = covert(x,y,z);
							newData[y][x][z] =checkPixelBounds(HP(pix)+data[y][x][z]);
						}
					}
				}
				drawImg(imagePanel2,newData);
				
			}
		});
	}// end of the constructor
	
	int [][] covert( int x , int y , int color ){
		int[][] center = new int[3][3];
		for (int i = 0;i<3;i++) {
			for(int j =0; j<3 ; j++) {
				int h = checkImageBounds(y-1+i,height);
				int w = checkImageBounds(x-1+i,width);
				center[i][j] = data[h][w][color];
			}
		}
		return center;
	}
	int HP(int[][]pix) {
		double value = 0.0;
		for(int i=0;i<pix.length;i++) {
			for(int j=0;j<pix[0].length;j++) {
				if(i==1&&j==1) {
					value +=(pix[i][j]*8.0/9.0);
				}
				else
					value -=(pix[i][j]*1.0/9.0);
			}
		}
		return (int)value;
	}
	int LP(int[][]pix) {
		double value = 0.0;
		for(int i=0;i<pix.length;i++) {
			for(int j =0 ;j<pix[0].length;j++) {
				value+=(pix[i][j]*1.0/9.0);
			}
		}
		return checkPixelBounds((int)value);
	}
	
	static int checkPixelBounds(int value){
		if (value >255) return 255;
		if (value <0) return 0;
		return value;
 	}
	static int getR(int rgb){
		  return checkPixelBounds((rgb & 0x00ff0000)>>>16);	
 }

	//get green channel from colorspace (4 bytes)
	static int getG(int rgb){
	  return checkPixelBounds((rgb & 0x0000ff00)>>> 8);
	}
	
	//get blue channel from colorspace (4 bytes)
	static int getB(int rgb){
		  return  checkPixelBounds(rgb & 0x000000ff);
	}
	
	static int makeColor(int r, int g, int b){
		return (255<< 24 | r<<16 | g<<8 | b);
	}
	
	static int checkImageBounds(int value, int length){
		 if (value > length-1) return length-1;
		 else if (value < 0) return 0;
		 else return value;
	}
	
	static BufferedImage makeImg(int[][][] newData){
		int height = newData.length;
		int width =  newData[0].length;
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
        for (int y=0; y<height; y++) {
        	for (int x=0; x<width; x++) {
        		int rgb = makeColor(newData[y][x][0],
        								 newData[y][x][1], 
        								 newData[y][x][2]);
        		newImg.setRGB(x, y, rgb);
        	}
        	
        }
        return newImg;
	}
	
	static void drawImg(ImagePanel panel, BufferedImage img) {
		Graphics g = panel.getGraphics();
		panel.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

	static void drawImg(ImagePanel panel, int[][][] newData) {
		BufferedImage img = makeImg(newData);
		drawImg(panel, img);
	}
	static int[][][] makeRGBData(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int[][][] data = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = getR(rgb);
				data[y][x][1] = getG(rgb);
				data[y][x][2] = getB(rgb);
			}
		}
		return data;
	}
	static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return img;
		
	}
	
	
	public static void main(String[] args) {
		FilterFrame_hw frame = new FilterFrame_hw();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}