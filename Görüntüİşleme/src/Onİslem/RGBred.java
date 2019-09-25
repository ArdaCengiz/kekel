package Onİslem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author Arda
 */
public class RGBred extends JFrame {

    BufferedImage myimg;
    int w = 500;
    int h = 500;
    int[] Rfreq;

    int rcolor;

    int rgb;
    long sumR;

    public RGBred(BufferedImage newimg) {
        setSize(500, 500);
        setBounds(0, 0, 500, 500);
        setTitle("RED Histogram");
        myimg = newimg;
        setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        Rfreq = new int[256];
        int width = myimg.getWidth();
        int height = myimg.getHeight();
        for (int i = 0; i < width; i++) {//Resim İçerisinde geziliyor
            for (int j = 0; j < height; j++) {
                //Bulunan pixelin rgb değeri alınıyor
                rgb = myimg.getRGB(i, j);
                rcolor = (rgb >> 16) & 0xff;
//Bulunan rgb değerinden kaçar tane olduğu diziye aktarılıyor
                Rfreq[rcolor]++;

            }
        }
//Gösetirilecek boyut
        int iw = w / 2;
        int ih = h / 2;
//her seviye için işlem yapılıyor
        for (int i = 0; i < 256; i++) {
//her değer için o değerden kaçar tane bulunduğuna göre cizim yapılyıor
            g2D.setColor(Color.RED);
            g2D.drawLine(20 + i, h - 20, 20 + i, h - (Rfreq[i] / 10) - 20);

            sumR += Rfreq[i] * i;
        }
        g.setColor(Color.BLACK);
        g2D.drawLine(10, h - 10, 10, h - 256);
        g2D.drawLine(10, h - 10, 256, h - 10);
        g2D.drawString("0-->255", 260, h - 10);
        g2D.setFont(new Font("Aril", Font.BOLD, 12));

        g2D.drawString("RGB Histogram", 300, 100);
      

    }

}
