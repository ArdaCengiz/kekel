package Onİslem;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import sun.awt.image.ToolkitImage;

public class Önİslem {

    private BufferedImage OrjinalResim;
    private BufferedImage SonuçResmi;
    private int ResimGenisligi, ResimYuksekligi;
    int drag_status = 0, x1, y1, x2, y2;

    public BufferedImage getOrjinalResim() {
        return OrjinalResim;
    }

    public BufferedImage getSonuçResmi() {
        return SonuçResmi;
    }

    public int getResimGenisligi() {
        return ResimGenisligi;
    }

    public int getResimYuksekligi() {
        return ResimYuksekligi;
    }

    public int getDrag_status() {
        return drag_status;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public void setOrjinalResim(BufferedImage OrjinalResim) {
        this.OrjinalResim = OrjinalResim;
    }

    public void setSonuçResmi(BufferedImage SonuçResmi) {
        this.SonuçResmi = SonuçResmi;
    }

    public void setResimGenisligi(int ResimGenisligi) {
        this.ResimGenisligi = ResimGenisligi;
    }

    public void setResimYuksekligi(int ResimYuksekligi) {
        this.ResimYuksekligi = ResimYuksekligi;
    }

    public void setDrag_status(int drag_status) {
        this.drag_status = drag_status;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public Önİslem(BufferedImage img) {
        this.OrjinalResim = img;
        this.ResimGenisligi = OrjinalResim.getWidth();
        this.ResimYuksekligi = OrjinalResim.getHeight();
        this.SonuçResmi = new BufferedImage(ResimGenisligi, ResimYuksekligi, OrjinalResim.TYPE_INT_RGB);
    }

    public BufferedImage GriyeCevir() {
        for (int x = 0; x < OrjinalResim.getWidth(); ++x) {
            for (int y = 0; y < OrjinalResim.getHeight(); ++y) {
                int rgb = OrjinalResim.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                SonuçResmi.setRGB(x, y, gray);
            }
        }

        return SonuçResmi;
    }

    public void RBGHistogram() {
        RGBred yeni = new RGBred(OrjinalResim);
        RGBGreen yeni1 = new RGBGreen(OrjinalResim);
        RGBBlue yeni2 = new RGBBlue(OrjinalResim);
    }

    public BufferedImage YenidenBoyutlandır(int x, int y) throws IOException {

        BufferedImage img = OrjinalResim;

        Image image = img.getScaledInstance(x, y, OrjinalResim.getType());
        SonuçResmi = new BufferedImage(x, y, OrjinalResim.getType());
        SonuçResmi.getGraphics().drawImage(image, 0, 0, null);
        return SonuçResmi;
    }

    public BufferedImage draggedScreen() throws AWTException, IOException {

        File save_path = new File("screen1.jpg");
        int w = Math.abs(x1 - x2);
        int h = Math.abs(y1 - y2);
        Robot robot = new Robot();
        if (x1 < x2 && y1 > y2) {
            System.out.println("şart1");
            SonuçResmi = robot.createScreenCapture(new Rectangle(x1, y2, w, h));

        } else if (x1 > x2 && y2 > y1) {
            System.out.println("şart2");
            SonuçResmi = robot.createScreenCapture(new Rectangle(x2, y1, w, h));

        } else if (x2 > x1 && y2 > y1) {
            System.out.println("şart3");
            SonuçResmi = robot.createScreenCapture(new Rectangle(x1, y1, w, h));

        } else if (x1 > x2 && y1 > y2) {
            System.out.println("şart4");
            SonuçResmi = robot.createScreenCapture(new Rectangle(x2, y2, w, h));

        }

        
        return SonuçResmi;
    }

}
