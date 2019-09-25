package Filtreleme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.Arrays;
import javaapplication1.NewJFrame;

/**
 *
 * @author Arda
 */
public class Filtre {

    private BufferedImage OrjinalResim;
    private BufferedImage SonuçResmi;

    public BufferedImage getSonuçResmi() {
        return SonuçResmi;
    }
    private int ResimGenisligi, ResimYuksekligi;

    public Filtre(BufferedImage img) {
        this.OrjinalResim = img;
        this.ResimGenisligi = OrjinalResim.getWidth();
        this.ResimYuksekligi = OrjinalResim.getHeight();
        this.SonuçResmi = new BufferedImage(ResimGenisligi, ResimYuksekligi, OrjinalResim.TYPE_INT_RGB);

    }

    public BufferedImage OrtaSon() {
        Color OkunanRenk;
        int SablonBoyutu = 15; //şablon boyutu 3 den büyük tek rakam olmalıdır (3,5,7 gibi).
        int ElemanSayisi = SablonBoyutu * SablonBoyutu;
        int[] R = new int[ElemanSayisi];
        int[] G = new int[ElemanSayisi];
        int[] B = new int[ElemanSayisi];
        int[] Gri = new int[ElemanSayisi];

        for (int x = (SablonBoyutu - 1) / 2; x < ResimGenisligi - (SablonBoyutu - 1) / 2; x++) {
            for (int y = (SablonBoyutu - 1) / 2; y < ResimYuksekligi - (SablonBoyutu - 1) / 2; y++) {
                //Şablon bölgesi (çekirdek matris) içindeki pikselleri tarıyor.
                int k = 0;
                for (int i = -((SablonBoyutu - 1) / 2); i <= (SablonBoyutu - 1) / 2; i++) {
                    for (int j = -((SablonBoyutu - 1) / 2); j <= (SablonBoyutu - 1) / 2; j++) {
                        OkunanRenk = new Color(OrjinalResim.getRGB(x + i, y + j));
                        R[k] = OkunanRenk.getRed();
                        G[k] = OkunanRenk.getGreen();
                        B[k] = OkunanRenk.getBlue();
                        Gri[k] = (short) (R[k] * 0.299 + G[k] * 0.587 + B[k] * 0.114);

                        k++;
                    }
                }
                //Gri tona göre sıralama yapıyor. Aynı anda üç rengide değiştiriyor.
                int GeciciSayi = 0;
                for (int i = 0; i < ElemanSayisi; i++) {
                    for (int j = i + 1; j < ElemanSayisi; j++) {
                        if (Gri[j] < Gri[i]) {
                            GeciciSayi = Gri[i];
                            Gri[i] = Gri[j];
                            Gri[j] = GeciciSayi;
                            GeciciSayi = R[i];
                            R[i] = R[j];
                            R[j] = GeciciSayi;
                            GeciciSayi = G[i];
                            G[i] = G[j];
                            G[j] = GeciciSayi;
                            GeciciSayi = B[i];
                            B[i] = B[j];
                            B[j] = GeciciSayi;
                        }
                    }
                }
                //Sıralama sonrası ortadaki değeri çıkış resminin piksel değeri olarak atıyor.

                SonuçResmi.setRGB(x, y, new Color(R[(ElemanSayisi - 1) / 2], G[(ElemanSayisi - 1)
                        / 2], B[(ElemanSayisi - 1) / 2]).getRGB());

            }
        }

        return SonuçResmi;
    }

    public BufferedImage Blur() {
        Color OkunanRenk;

        int maskeBoyutu = 8;
        int toplamRED, toplamGREEN, toplamBlue;
        int ortalamaGREEN, ortalamaBLUE, ortalamaRED;

        for (int x = (maskeBoyutu - 1) / 2; x < ResimGenisligi - (maskeBoyutu - 1) / 2; x++) {
            for (int y = (maskeBoyutu - 1) / 2; y < ResimYuksekligi - (maskeBoyutu - 1) / 2; y++) {
                toplamRED = 0;
                toplamGREEN = 0;
                toplamBlue = 0;
                for (int i = -((maskeBoyutu - 1) / 2); i <= (maskeBoyutu - 1) / 2; i++) {
                    for (int j = -((maskeBoyutu - 1) / 2); j <= (maskeBoyutu - 1) / 2; j++) {
                        OkunanRenk = new Color(OrjinalResim.getRGB(x + i, y + j));
                        toplamRED = toplamRED + OkunanRenk.getRed();
                        toplamGREEN = toplamGREEN + OkunanRenk.getGreen();
                        toplamBlue = toplamBlue + OkunanRenk.getBlue();
                    }
                }

                ortalamaRED = toplamRED / (maskeBoyutu * maskeBoyutu);
                ortalamaGREEN = toplamGREEN / (maskeBoyutu * maskeBoyutu);
                ortalamaBLUE = toplamBlue / (maskeBoyutu * maskeBoyutu);
                Color myWhite = new Color(ortalamaRED, ortalamaGREEN, ortalamaBLUE);
                SonuçResmi.setRGB(x, y, myWhite.getRGB());

            }
        }
        return SonuçResmi;
    }

    public BufferedImage Keskinleştirme() {
        Color OkunanRenk;

        int SablonBoyutu = 3;

        int toplamRED, toplamGREEN, toplamBlue;
        int R, G, B;
        int[] Matris = {0, -2, 0, -2, 11, -2, 0, -2, 0};
        int MatrisToplami = 0;

        for (int z = 0; z < Matris.length; z++) {
            MatrisToplami += Matris[z];
        }

        for (int x = (SablonBoyutu - 1) / 2; x < ResimGenisligi - (SablonBoyutu - 1) / 2; x++) //Resmi taramaya şablonun yarısı kadar dış kenarlardan içeride başlayacak ve bitirecek.
        {
            for (int y = (SablonBoyutu - 1) / 2; y < ResimYuksekligi - (SablonBoyutu - 1) / 2; y++) {
                toplamRED = 0;
                toplamGREEN = 0;
                toplamBlue = 0;
                //Şablon bölgesi (çekirdek matris) içindeki pikselleri tarıyor.
                int k = 0; //matris içindeki elemanları sırayla okurken kullanılacak.
                for (int i = -((SablonBoyutu - 1) / 2); i <= (SablonBoyutu - 1) / 2; i++) {
                    for (int j = -((SablonBoyutu - 1) / 2); j <= (SablonBoyutu - 1) / 2; j++) {
                        OkunanRenk = new Color(OrjinalResim.getRGB(x + i, y + j));
                        toplamRED = toplamRED + OkunanRenk.getRed() * Matris[k];
                        toplamGREEN = toplamGREEN + OkunanRenk.getGreen() * Matris[k];
                        toplamBlue = toplamBlue + OkunanRenk.getBlue() * Matris[k];
                        R = toplamRED / MatrisToplami;
                        G = toplamGREEN / MatrisToplami;
                        B = toplamBlue / MatrisToplami;

                        //Renkler sınırların dışına çıktıysa, sınır değer alınacak.
                        if (R > 255) {
                            R = 255;
                        }
                        if (G > 255) {
                            G = 255;
                        }
                        if (B > 255) {
                            B = 255;
                        }
                        if (R < 0) {
                            R = 0;
                        }
                        if (G < 0) {
                            G = 0;
                        }
                        if (B < 0) {
                            B = 0;
                        }
                        //===========================================================

                        Color myWhite = new Color(R, G, B);
                        SonuçResmi.setRGB(x, y, myWhite.getRGB());
                        k++;
                    }
                }
            }
        }

        return SonuçResmi;
    }

    public BufferedImage laplace() {
        for (int y = 1; y < ResimYuksekligi - 1; y++) {
            for (int x = 1; x < ResimGenisligi - 1; x++) {
                Color c00 = new Color(OrjinalResim.getRGB(x - 1, y - 1));
                Color c01 = new Color(OrjinalResim.getRGB(x - 1, y));
                Color c02 = new Color(OrjinalResim.getRGB(x - 1, y + 1));
                Color c10 = new Color(OrjinalResim.getRGB(x, y - 1));
                Color c11 = new Color(OrjinalResim.getRGB(x, y));
                Color c12 = new Color(OrjinalResim.getRGB(x, y + 1));
                Color c20 = new Color(OrjinalResim.getRGB(x + 1, y - 1));
                Color c21 = new Color(OrjinalResim.getRGB(x + 1, y));
                Color c22 = new Color(OrjinalResim.getRGB(x + 1, y + 1));
                int r = -c00.getRed() - c01.getRed() - c02.getRed()
                        + -c10.getRed() + 8 * c11.getRed() - c12.getRed()
                        + -c20.getRed() - c21.getRed() - c22.getRed();
                int g = -c00.getGreen() - c01.getGreen() - c02.getGreen()
                        + -c10.getGreen() + 8 * c11.getGreen() - c12.getGreen()
                        + -c20.getGreen() - c21.getGreen() - c22.getGreen();
                int b = -c00.getBlue() - c01.getBlue() - c02.getBlue()
                        + -c10.getBlue() + 8 * c11.getBlue() - c12.getBlue()
                        + -c20.getBlue() - c21.getBlue() - c22.getBlue();
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                Color color = new Color(r, g, b);
                SonuçResmi.setRGB(x, y, color.getRGB());
            }
        }

        return SonuçResmi;

    }

    public static int truncate(int a) {
        if (a < 0) {
            return 0;
        } else if (a > 255) {
            return 255;
        } else {
            return a;
        }
    }

    public static double intensity(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return 0.299 * r + 0.587 * g + 0.114 * b;
    }

    public BufferedImage kenar() {

        int[][] filter1 = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        };

        int[][] filter2 = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
        };

        for (int y = 1; y < ResimYuksekligi - 1; y++) {
            for (int x = 1; x < ResimGenisligi - 1; x++) {

                int[][] gray = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray[i][j] = (int) intensity(new Color(OrjinalResim.getRGB(x - 1 + i, y - 1 + j)));
                    }
                }

                int gray1 = 0, gray2 = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray1 += gray[i][j] * filter1[i][j];
                        gray2 += gray[i][j] * filter2[i][j];
                    }
                }

                int magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
                Color grayscale = new Color(magnitude, magnitude, magnitude);
                SonuçResmi.setRGB(x, y, grayscale.getRGB());
            }
        }

        return SonuçResmi;
    }
}
