package ru.albemuth;

import org.junit.Test;
import ru.albemuth.util.Distribution;
import ru.albemuth.util.RandomGenerator;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.fail;


public class TestSimple {

    @Test
    public void test() {
        try {
            Map<String, Long> files = new HashMap<String, Long>();
            File source = new File("D:/Мои рисунки/");
            long srcLen = vizitSource(files, source);
            System.out.println(files.size());
            long dstLen = vizitDest(files);
            System.out.println(srcLen);
            System.out.println(dstLen);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private long vizitSource(Map<String, Long> files, File source) {
        long len = 0;
        File[] children = source.listFiles();
        if (children == null) {
            System.err.println(source.getAbsolutePath());
        } else {
            for (File file: source.listFiles()) {
                if (file.isDirectory()) {
                    len += vizitSource(files, file);
                } else {
                    long l = file.length();
                    if (files.put(file.getAbsolutePath(), l) != null) {
                        System.err.println(file.getAbsolutePath());
                    }
                    len += l;
                }
            }
        }
        return len;
    }

    private long vizitDest(Map<String, Long> files) {
        long len = 0;
        for (String fileName: files.keySet()) {
            File file = new File("F:" + fileName.substring("D:".length()));
            if (!file.exists()) {
                System.out.println(fileName);
            }
            long l = file.length();
            if (l != files.get(fileName)) {
                System.out.println(fileName + ", " + files.get(fileName) + ", " + l);
            }
            len += l;
        }
        return len;
    }

    public static void elect(double q, boolean verbose) {
        int electionsNumber = 1000;//кол-во экспериментов
        int pollibgStationsNumber = 90000;//90 тысяч избирательных участков

        Random r = new Random();

        int has49Peak = 0;
        int has50Peak = 0;
        int has51Peak = 0;
        int _48value;
        int _49value;
        int _50value;
        int _51value;
        int _52value;
        for (int j = 0; j < electionsNumber; j++) {
            _48value = 0;
            _49value = 0;
            _50value = 0;
            _51value = 0;
            _52value = 0;
            for (int i = 0; i < pollibgStationsNumber; i++) {
                int n = r.nextInt(501) + 500;//кол-во избирателей на участке - равномерно распределено от 500 до 1000 включительно
                int k = r.nextInt(n + 1);//колво голосов за партию - равномерно распределено от 0 до кол-ва избирателей включительно
                double p = ((double) (100 * k)) / ((double) n);//процент голосов за партию на участке
                boolean changed = false;
                if (Math.abs(50 - 4 * q - p) < q) {
                    _48value++;
                    changed = true;
                }
                if (Math.abs(50 - 2 * q - p) < q) {
                    _49value++;
                    changed = true;
                }
                if (Math.abs(50 - p) < q) {
                    _50value++;
                    changed = true;
                }
                if (Math.abs(50 + 2 * q - p) < q) {
                    _51value++;
                    changed = true;
                }
                if (Math.abs(50 + 4 * q - p) < q) {
                    _52value++;
                    changed = true;
                }
                if (changed && verbose) {
                    System.out.printf("%d : %d/%d = %G : %d %d %d %d %d\n", j, k, n, p, _48value, _49value, _50value, _51value, _52value);
                }
            }
            if (_48value < _49value && _49value > _50value) {
                has49Peak++;
            }
            if (_49value < _50value && _50value > _51value) {
                has50Peak++;
            }
            if (_50value < _51value && _51value > _52value) {
                has51Peak++;
            }
        }
        System.out.println(q + ": " + has49Peak + ", " + (has49Peak / (double) electionsNumber) + ", " + has50Peak + ", " + (has50Peak / (double) electionsNumber) + ", " + has51Peak + ", " + (has51Peak / (double) electionsNumber));

    }

    @Test
    public void testElections() {
        int has49Peak = 0;
        int has50Peak = 0;
        int has51Peak = 0;
        long t1 = System.currentTimeMillis();
        int electionsNumber = 1000;//кол-во экспериментов
        int pollibgStationsNumber = 90000;//90 тысяч избирательных участков
        List<Double> l = new ArrayList<Double>(pollibgStationsNumber);
        //for (int j = 0; j < electionsNumber; j++) {
        for (int i = 0; i < pollibgStationsNumber; i++) {
            int n = RandomGenerator.randomInt(500, 1001);//кол-во избирателей на участке - равномерно распределено от 500 до 1000 включительно
            int k = RandomGenerator.randomInt(0, n+1);//колво голосов за партию - равномерно распределено от 0 до кол-ва избирателей включительно
            //l.add(Math.round(100*k/(double)n)/(double)100);//процент голосов за партию на участке
            l.add((1000 * k/n)/(double)1000);

        }
        Map<Double, Distribution.Group<Double>> groups = new Distribution<Double>().calculate(l);//строим распределение колва участков от процента голосов за партию
        int _48value = groups.get(0.498).getNumber();//кол-во участков с 48%
        int _49value = groups.get(0.499).getNumber();//кол-во участков с 49%
        int _50value = groups.get(0.5).getNumber();//кол-во участков с 50%
        int _51value = groups.get(0.501).getNumber();//кол-во участков с 51%
        int _52value = groups.get(0.502).getNumber();//кол-во участков с 52%
        if (_48value < _49value && _49value > _50value) {//пик на 49%
            has49Peak++;
        }
        if (_49value < _50value && _50value > _51value) {//пик на 50%
            has50Peak++;
        }
        if (_50value < _51value && _51value > _52value) {//пик на 51%
            has51Peak++;
        }
        l.clear();
        //Distribution.printGroups(Distribution.numberSortedGroups(groups));
        Distribution.printGroups(Distribution.valueSortedGroups(groups));
        //}
        //System.out.println((System.currentTimeMillis() - t1) + ", " + has49Peak + ", " +  (has49Peak/(double)electionsNumber) + ", " + has50Peak + ", " +  (has50Peak/(double)electionsNumber) + ", " + has51Peak + ", " +  (has51Peak/(double)electionsNumber));
    }

    @Test
    public void test2() {
        try {
            int[] h = new int[101];
            for (int x = 1; x <= 500; x++) {
                for (int y = x; y <= 500; y++) {
                    int r = x*100/y;
                    h[r] = h[r] + 1;
                }
            }
            for (int i = 0; i < h.length; i++) {
                System.out.println(i + "\t" + h[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}

