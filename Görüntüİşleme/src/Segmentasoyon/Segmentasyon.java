
package Segmentasoyon;

import Onİslem.Önİslem;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 * @author Arda
 */
public class Segmentasyon {

    private BufferedImage OrjinalResim;
    private BufferedImage SonuçResmi;

    public BufferedImage getSonuçResmi() {
        return SonuçResmi;
    }
    private int ResimGenisligi, ResimYuksekligi;
    
    public Segmentasyon(BufferedImage img) {
        this.OrjinalResim = img;
        this.ResimGenisligi = OrjinalResim.getWidth();
        this.ResimYuksekligi = OrjinalResim.getHeight();
        this.SonuçResmi = new BufferedImage(ResimGenisligi, ResimYuksekligi, OrjinalResim.getType());

    }

    public int EsikDegeriHesapla(BufferedImage img) {

        int thresholdValue = 127, iThreshold;

        int sum1, sum2, count1, count2;

        int mean1, mean2;

        while (true) {
            sum1 = sum2 = count1 = count2 = 0;
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {

                    Color OkunanRenk = new Color(img.getRGB(x, y));
                    int r = OkunanRenk.getRed();
                    int g = OkunanRenk.getGreen();
                    int b = OkunanRenk.getBlue();
                    int avgOfRGB = (r + g + b) / 3;

                    if (avgOfRGB < thresholdValue) {
                        sum1 += avgOfRGB;
                        count1++;
                    } else {
                        sum2 += avgOfRGB;
                        count2++;
                    }
                }
            }

            mean1 = (count1 > 0) ? (int) (sum1 / count1) : 0;
            mean2 = (count2 > 0) ? (int) (sum2 / count2) : 0;

            
            iThreshold = (mean1 + mean2) / 2;

            if (thresholdValue != iThreshold) {
                thresholdValue = iThreshold;
            } else {
                break;
            }
        }
      
     
        return thresholdValue;
    }
    
    public BufferedImage EsikDegeriUygula(BufferedImage img,int EsikDegeri){
         for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int p = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
                if (p >= EsikDegeri) {

                    img.setRGB(x, y, new Color(0, 0, 0, 255).getRGB());
                } else {
                    img.setRGB(x, y, new Color(255, 255, 255, 255).getRGB());

                }
            }
        }
        return img;
    }

    public BufferedImage dörkenar(BufferedImage img) {
        int[][] arda = new int[img.getWidth()][img.getHeight()];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color OkunanRenk = new Color(img.getRGB(x, y));
                int değer = OkunanRenk.getRed();
                if (değer == 255 && x + 1 < img.getWidth() && x - 1 > 0 && y + 1 < img.getHeight() && y - 1 > 0) {
                    OkunanRenk = new Color(img.getRGB(x, y - 1));
                    int komsu1, komsu2, komsu3, komsu4;
                    komsu1 = OkunanRenk.getRed();
                    OkunanRenk = new Color(img.getRGB(x - 1, y));
                    komsu2 = OkunanRenk.getRed();
                    OkunanRenk = new Color(img.getRGB(x, y + 1));
                    komsu3 = OkunanRenk.getRed();
                    OkunanRenk = new Color(img.getRGB(x + 1, y));
                    komsu4 = OkunanRenk.getRed();
                    if (komsu1 == 255 || komsu2 == 255 || komsu3 == 255 || komsu4 == 255) {
                        arda[x][y] = 1;
                   
                    } else {
                        arda[x][y] = 0;
                    }
                }

            }
        }
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (arda[x][y] == 1) {
                    img.setRGB(x, y, new Color(255, 0, 0).getRGB());
                }

            }
        }

        return img;
    }

    public BufferedImage RenkliResimdeNesneBul(BufferedImage GirisResmi) {
        BufferedImage CikisResmi = GirisResmi;
        int KomsularinEnKucukEtiketDegeri = 0;

        int ResimGenisligi = GirisResmi.getWidth();
        int ResimYuksekligi = GirisResmi.getHeight();
        int PikselSayisi = ResimGenisligi * ResimYuksekligi;
        int esikdegeri= EsikDegeriHesapla(GirisResmi);
        GirisResmi = EsikDegeriUygula(GirisResmi,esikdegeri);

        int x, y, i, j, EtiketNo = 0;
        int[][] EtiketNumarasi = new int[ResimGenisligi][ResimYuksekligi];
        for (x = 0; x < ResimGenisligi; x++) {
            for (y = 0; y < ResimYuksekligi; y++) {
                EtiketNumarasi[x][y] = 0;
            }
        }
        int IlkDeger = 0, SonDeger = 0;
        boolean DegisimVar = false;
        do //etiket numaralarında değişim kalmayana kadar dönecek.
        {
            DegisimVar = false;
            //------------------------- Resmi tarıyor ----------------------------

            for (y = 1; y < ResimYuksekligi - 1; y++) //Resmin 1 piksel içerisinden başlayıp, bitirecek. Çünkü
            {
                for (x = 1; x < ResimGenisligi - 1; x++) {
                    //Resim siyah beyaz olduğu için tek kanala bakmak yeterli olacak. Sıradaki piksel beyaz ise işlem
                    Color color = new Color(GirisResmi.getRGB(x, y));
                    if (color.getRed() > 128) {

                        IlkDeger = EtiketNumarasi[x][y];
                        //Komşular arasında en küçük etiket numarasını bulacak.
                        KomsularinEnKucukEtiketDegeri = 0;
                        for (j = -1; j <= 1; j++) //Çekirdek şablon 3x3 lük bir matris. Dolayısı ile x,y nin -1 den başlayıp +1
                        {
                            for (i = -1; i <= 1; i++) {
                                if (EtiketNumarasi[x + i][y + j] != 0 && KomsularinEnKucukEtiketDegeri == 0) //hücrenin
                                {
                                    KomsularinEnKucukEtiketDegeri = EtiketNumarasi[x + i][y + j];
                                } else if (EtiketNumarasi[x + i][y + j] < KomsularinEnKucukEtiketDegeri && EtiketNumarasi[x
                                        + i][y + j] != 0 && KomsularinEnKucukEtiketDegeri != 0) //En küçük değer ve okunan hücreye etiket atanmışsa,
                                {
                                    KomsularinEnKucukEtiketDegeri = EtiketNumarasi[x + i][y + j];
                                }
                            }
                        }
                        if (KomsularinEnKucukEtiketDegeri != 0) //Beyaz komşu buldu ve içlerinde en küçük etiket
                        {
                            EtiketNumarasi[x][y] = KomsularinEnKucukEtiketDegeri;
                        } else if (KomsularinEnKucukEtiketDegeri == 0) //Komşuların hiç birinde etiket numarası yoksa o
                        {
                            EtiketNo = EtiketNo + 1;
                            EtiketNumarasi[x][y] = EtiketNo;
                        }
                        SonDeger = EtiketNumarasi[x][y]; //İşlem öncesi ve işlem sonrası değerler aynı ise ve butün

                        if (IlkDeger != SonDeger) {
                            DegisimVar = true;
                        }
                    }
                }
            }
        } while (DegisimVar == true);
        int[] DiziEtiket = new int[PikselSayisi];
        i = 0;
        for (x = 1; x < ResimGenisligi - 1; x++) {
            for (y = 1; y < ResimYuksekligi - 1; y++) {
                i++;
                DiziEtiket[i] = EtiketNumarasi[x][y];
            }
        }
        //Dizideki etiket numaralarını sıralıyor. Hazır fonksiyon kullanıyor.

        Arrays.sort(DiziEtiket);
        //Tekrar eden etiket numaraarını çıkarıyor. Hazır fonksiyon kullanıyor.
        int[] TekrarsizEtiketNumaralari = IntStream.of(DiziEtiket).distinct().toArray();

        int[] RenkDizisi = new int[TekrarsizEtiketNumaralari.length];

        for (j = 0; j < TekrarsizEtiketNumaralari.length; j++) {
            RenkDizisi[j] = TekrarsizEtiketNumaralari[j]; //sıradaki ilk renge, ait olacağı etiketin kaç numara

        }
        int RenkSayisi = RenkDizisi.length;
        Color[] Renkler = new Color[RenkSayisi];
        Random Rastgele = new Random();
        int Kirmizi, Yesil, Mavi;
        for (int r = 0; r < RenkSayisi; r++) //sonraki renkler.
        {

            Kirmizi = (Rastgele.nextInt((25 - 5) + 1) + 5) * 10;

            Yesil = (Rastgele.nextInt((25 - 5) + 1) + 5) * 10;
            Mavi = (Rastgele.nextInt((25 - 5) + 1) + 5) * 10;

            Renkler[r] = new Color(Kirmizi, Yesil, Mavi);
        }

        for (x = 1; x < ResimGenisligi - 1; x++) //Resmin 1 piksel içerisinden başlayıp, bitirecek. Çünkü çekirdek
        {
            for (y = 1; y < ResimYuksekligi - 1; y++) {

                int RenkSiraNo = Arrays.binarySearch(RenkDizisi, EtiketNumarasi[x][y]);

                Color color = new Color(GirisResmi.getRGB(x, y));
                if (color.getRed() < 128) //Eğer bu pikselin rengi siyah ise aynı pikselin CikisResmi
                {
                    CikisResmi.setRGB(x, y, Color.black.getRGB());

                } else {

                    CikisResmi.setRGB(x, y, Renkler[RenkSiraNo].getRGB());

                }

            }
        }
        return CikisResmi;

    }
}
