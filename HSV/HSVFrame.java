import java.awt.Graphics;
import java.awt.GridLayout;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSVFrame  extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelHue = new JPanel();
	JPanel cotrolPanelSat = new JPanel();
	JPanel cotrolPanelVal = new JPanel();
	JPanel cotrolPanelBin = new JPanel();
	JPanel cotrolPanelHSV = new JPanel();
	JPanel imagePanelOrg;  //@@ modified here 
	JPanel imagePanelHSV;  //@@ modified here 
	JButton btnShow;
	JSlider sliderHue;
	JSlider sliderSat;
	JSlider sliderVal;
	JLabel lbHue = new JLabel("    Hue");
	JLabel lbSat = new JLabel("    Saturation");
	JLabel lbLum = new JLabel("    Value");
	JLabel lbHueBeging = new JLabel("-180");
	JLabel lbHueEnd = new JLabel("180");
	JLabel lbSatBeging = new JLabel("-100(%)") ;
	JLabel lbSatEnd =  new JLabel("100(%)") ;;
	JLabel lbLumBeging  = new JLabel("-100(%)") ;;
	JLabel lbLumEnd  = new JLabel("100(%)") ;;
	JTextField tfHueValue = new JTextField(3);
	JTextField tfSatValue = new JTextField(3);
	JTextField tfValValue = new JTextField(3);
	int[][][] data;
	int height;
	int width;
	static BufferedImage imgOrg = null;   //@@ modified here 
	static BufferedImage imgHSV = null; //@@ modified here
	
	HSVFrame() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("HSV Demo");
		btnShow = new JButton("Show Original Image");
		tfHueValue.setText("0");
		tfSatValue.setText("0");
		tfValValue.setText("0");
		tfHueValue.setEditable(false);
		tfSatValue.setEditable(false);
		tfValValue.setEditable(false);
		
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(6, 1));
		sliderHue = new JSlider(-180, 180, 0);
		cotrolPanelShow.add(btnShow);
		cotrolPanelHue.add(lbHueBeging);
		cotrolPanelHue.add(sliderHue);
		cotrolPanelHue.add(lbHueEnd);
		cotrolPanelHue.add(tfHueValue);
		cotrolPanelHue.add(lbHue);
		
		sliderSat = new JSlider(-100, 100, 0);
		cotrolPanelSat.add(lbSatBeging);
		cotrolPanelSat.add(sliderSat);
		cotrolPanelSat.add(lbSatEnd);
		cotrolPanelSat.add(tfSatValue);
		cotrolPanelSat.add(lbSat);
		
		sliderVal = new JSlider(-100, 100, 0);
		cotrolPanelVal.add(lbLumBeging);
		cotrolPanelVal.add(sliderVal);
		cotrolPanelVal.add(lbLumEnd);
		cotrolPanelVal.add(tfValValue);
		cotrolPanelVal.add(lbLum);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.add(cotrolPanelHue);
		cotrolPanelMain.add(cotrolPanelSat);
		cotrolPanelMain.add(cotrolPanelVal);
		cotrolPanelMain.add(cotrolPanelHSV);
		cotrolPanelMain.add(cotrolPanelBin);
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);
		imagePanelOrg = new ImagePanelLeft();  //@@ modified here 
		imagePanelOrg.setBounds(0, 220, 700, 700); //@@ modified here 
		getContentPane().add(imagePanelOrg); //@@ modified here 
		imagePanelHSV = new ImagePanelRight(); //@@ modified here 
		imagePanelHSV.setBounds(750, 220, 700, 700); //@@ modified here 
		getContentPane().add(imagePanelHSV); //@@ modified here 

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Graphics g = imagePanelOrg.getGraphics(); //@@ modified here 
				g.drawImage(imgOrg, 0, 0, null); //@@ modified here 
			}
		});
	    
		sliderHue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfHueValue.setText(sliderHue.getValue() + "");
				doHSV();
			}
		});

		sliderSat.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfSatValue.setText(sliderSat.getValue() + "");
				doHSV();
			}
		});

		sliderVal.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfValValue.setText(sliderVal.getValue() + "");
				doHSV();
			}
		});
	}
     
	int [][][] hsvAdjust(int[][][] data, double hueOffset, double satOffset, double valOffset) {
		double hue;
		double sat;
		double val;
		RGB rgb;
		int [][][] newData=  new int [height][width][3];
		for (int ii = 0; ii < height; ii++) {
			for (int jj = 0; jj < width; jj++) {
				int r = data[ii][jj][0];
				int g = data[ii][jj][1];
				int b = data[ii][jj][2];
				HSV hsv = Util.RGB2HSV(r, g, b);
				hue = hsv.h;
				sat = hsv.s;
				val = hsv.v;
				hue += hueOffset;
				if (satOffset > 0.0)
					sat = Util.linear(sat, 1, satOffset);
				if (satOffset < 0.0)
					sat = sat - satOffset * (0 - sat);
				if (valOffset > 0.0)
					val = Util.linear(val, 1, valOffset);
				if (valOffset < 0.0)
					val = val - valOffset * (0 - val);
				rgb = Util.HSV2RGB(hue, sat, val);
				newData[ii][jj][0] = rgb.r;
				newData[ii][jj][1] = rgb.g;
				newData[ii][jj][2] = rgb.b;
			}
		}
		return newData;
	}

	void doHSV() {
		double hueOffset = sliderHue.getValue();
		double satOffset = sliderSat.getValue();
		double valOffset = sliderVal.getValue();
		int [][][] newData = hsvAdjust(data, hueOffset, satOffset / 100.0, valOffset / 100.0);
		imgHSV = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
		Util.imgRight = imgHSV;
        for (int y=0; y<height; y++) {
        	for (int x=0; x<width; x++) {
        		int rgb = Util.makeColor(newData[y][x][0],
        								 newData[y][x][1], 
        								 newData[y][x][2]);
        		imgHSV.setRGB(x, y, rgb);
        	}
        }
        Graphics g = imagePanelHSV.getGraphics(); //@@ modified here 
		g.drawImage(imgHSV, 0, 0, null); //@@ modified here 
	}

	void loadImg() {
		try {
			// img = ImageIO.read(new File("file/plate.png"));
			imgOrg = ImageIO.read(new File("file/munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		Util.imgLeft = imgOrg;
		height = imgOrg.getHeight();
		width = imgOrg.getWidth();
		data = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = imgOrg.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
	}
	
	public static void main(String[] args) {
		HSVFrame frame = new HSVFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
