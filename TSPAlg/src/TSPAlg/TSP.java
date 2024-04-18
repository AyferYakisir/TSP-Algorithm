package TSPAlg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import TSPAlg.TSP.Pair;

public class TSP {

    // İki şehir arasındaki mesafeyi hesapla
    static double mesafeHesapla(Sehir a, Sehir b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    // En Yakın Komşu algoritması
    static class Pair {
        ArrayList<Integer> yol;
        double maliyet;

        Pair(ArrayList<Integer> yol, double maliyet) {
            this.yol = yol;
            this.maliyet = maliyet;
        }
    }

    static Pair enYakinKomsu(ArrayList<Sehir> sehirler) {
        int n = sehirler.size();
        boolean[] ziyaretEdildi = new boolean[n]; // Ziyaret edilip edilmediğini takip etmek için dizi
        ArrayList<Integer> yol = new ArrayList<>(); // En kısa yolun indekslerini tutacak liste

        int simdiki = 0; // Başlangıç şehri
        ziyaretEdildi[simdiki] = true;
        yol.add(simdiki);

        double toplamMaliyet = 0.0;

        for (int i = 1; i < n; ++i) {
            double minMesafe = Double.MAX_VALUE; // Minimum mesafeyi saklamak için başlangıçta sonsuz değer
            int enYakinSehir = -1;

            // Şu anki şehre en yakın olmayan ziyaret edilmemiş şehri bul
            for (int j = 0; j < n; ++j) {
                if (!ziyaretEdildi[j]) {
                    double mesafe = mesafeHesapla(sehirler.get(simdiki), sehirler.get(j));
                    if (mesafe < minMesafe) {
                        minMesafe = mesafe;
                        enYakinSehir = j;
                    }
                }
            }

            // En yakın şehri ziyaret et
            ziyaretEdildi[enYakinSehir] = true;
            yol.add(enYakinSehir);
            simdiki = enYakinSehir;

            // Bu adımın maliyetini toplam maliyete ekle
            toplamMaliyet += minMesafe;
        }

        // Başlangıç şehrine dönüş maliyetini hesapla ve toplam maliyetten çıkar
        toplamMaliyet -= mesafeHesapla(sehirler.get(yol.get(yol.size() - 1)), sehirler.get(yol.get(0)));

        return new Pair(yol, toplamMaliyet);
    }

    public static void main(String[] args) {
        long baslangic_zamani = System.nanoTime();
        // Dosyadan şehirleri oku
        File dosya = new File("C:\\Users\\Ayfer\\OneDrive\\Masaüstü\\tsp_85900_1.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(dosya);
        } catch (FileNotFoundException e) {
            System.out.println("Dosya açılamadı!");
            return;
        }

        int n = scanner.nextInt(); // Şehir sayısı
        ArrayList<Sehir> sehirler = new ArrayList<>(); // Şehirleri saklamak için liste

        // Şehir koordinatlarını oku
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            sehirler.add(new Sehir(x, y));
        }

        scanner.close();

        // En yakın komşu algoritmasını uygula
        Pair sonuc = enYakinKomsu(sehirler);

        long bitis_zamani = System.nanoTime();
        double gecen_zaman = (bitis_zamani - baslangic_zamani) / 1e9; // saniye cinsinden geçen zaman

        // Sonucu ekrana yazdır
        System.out.print("Optimal yol: ");
        for (int i = 0; i < sonuc.yol.size(); ++i) {
            System.out.print(sonuc.yol.get(i));
            if (i != sonuc.yol.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        System.out.println("Algoritmanın çalışma süresi: " + String.format("%.3f", gecen_zaman) + " saniye");
        
        System.out.println("Optimal maliyet değeri: " + sonuc.maliyet);
    }
}