
package Morfolojik;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Arda
 */
public class Morfolojik {

    private BufferedImage OrjinalResim;
    private BufferedImage SonuçResmi;

    public BufferedImage getSonuçResmi() {
        return SonuçResmi;
    }
    private int ResimGenisligi, ResimYuksekligi;

    public Morfolojik(BufferedImage img) {
        this.OrjinalResim = img;
        this.ResimGenisligi = OrjinalResim.getWidth();
        this.ResimYuksekligi = OrjinalResim.getHeight();
        this.SonuçResmi = new BufferedImage(ResimGenisligi, ResimYuksekligi, OrjinalResim.TYPE_INT_RGB);

    }

    public BufferedImage Genişletme(BufferedImage img, int mask[], int maskSize) {

        int width = img.getWidth();
        int height = img.getHeight();

       
        int buff[];

    
        int output[] = new int[width * height];

      
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buff = new int[maskSize * maskSize];
                int i = 0;
                for (int ty = y - maskSize / 2, mr = 0; ty <= y + maskSize / 2; ty++, mr++) {
                    for (int tx = x - maskSize / 2, mc = 0; tx <= x + maskSize / 2; tx++, mc++) {
                        /**
                         *  [0,1, 0 1, 1, 1 0, 1, 0]
                        */
                        if (ty >= 0 && ty < height && tx >= 0 && tx < width) {
                           

                            if (mask[mc + mr * maskSize] != 1) {
                                continue;
                            }
                            Color OkunanRenk = new Color(img.getRGB(tx, ty));
                            buff[i] = OkunanRenk.getRed();
                            i++;
                        }
                    }
                }

              
                java.util.Arrays.sort(buff);

               
                output[x + y * width] = buff[(maskSize * maskSize) - 1];
            }
        }

      
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = output[x + y * width];
                //img.setPixel(x, y, 255, v, v, v);
                SonuçResmi.setRGB(x, y, new Color(v, v, v, 255).getRGB());
            }
        }
        return SonuçResmi;
    }

    public BufferedImage Erezyon(BufferedImage img, int mask[], int maskSize) {

       
        int width = img.getWidth();
        int height = img.getHeight();

       
        int buff[];

   
        int output[] = new int[width * height];

    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buff = new int[maskSize * maskSize];
                int i = 0;
                for (int ty = y - maskSize / 2, mr = 0; ty <= y + maskSize / 2; ty++, mr++) {
                    for (int tx = x - maskSize / 2, mc = 0; tx <= x + maskSize / 2; tx++, mc++) {
                        /**
                         [0,1, 0 1, 1, 1 0, 1, 0]
                      */
                        if (ty >= 0 && ty < height && tx >= 0 && tx < width) {
                            
                            if (mask[mc + mr * maskSize] != 1) {
                                continue;
                            }
                            Color OkunanRenk = new Color(img.getRGB(tx, ty));
                            buff[i] = OkunanRenk.getRed();
                            i++;
                        }
                    }
                }

      
                java.util.Arrays.sort(buff);

                
                output[x + y * width] = buff[(maskSize * maskSize) - i];
            }
        }

        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = output[x + y * width];
               
                SonuçResmi.setRGB(x, y, new Color(v, v, v, 255).getRGB());
            }
        }
        return SonuçResmi;
    }

    public BufferedImage Openin(int mask[], int maskSize) {

        SonuçResmi = Erezyon(OrjinalResim, mask, maskSize);
        SonuçResmi = Genişletme(SonuçResmi, mask, maskSize);
        return SonuçResmi;
    }

    public BufferedImage Closing(int mask[], int maskSize) {
        SonuçResmi = Genişletme(OrjinalResim, mask, maskSize);
        SonuçResmi = Erezyon(SonuçResmi, mask, maskSize);
        return SonuçResmi;
    }

}

