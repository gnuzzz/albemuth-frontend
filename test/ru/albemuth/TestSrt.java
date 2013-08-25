package ru.albemuth;

import ru.albemuth.frontend.TestFrontend;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 24.10.2007
 * Time: 1:52:57
 */
public class TestSrt extends TestFrontend {

    private static final Pattern SUB        = Pattern.compile("(\\d+)\\r\\n(\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) --> (\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d)\\r\\n((?:[^\\r\\n]+\\r\\n)+)\\r\\n", Pattern.DOTALL);

    private DateFormat df = new SimpleDateFormat("HH:mm:ss,SSS");

    public void testNothing() {/* do nothing */}

    public void _test() {
        processFile("09", -1800);
        processFile("10", 1500);
        processFile("11", -1500);
        processFile("12", 1200);
        processFile("13", -1500);
    }

    public void processFile(String index, int shiftTimeMillis) {
        try {
            String content = readFile("C:/Programs/Video/Seikai no Saga/Banner of the Stars I/subs/Seikai_no_Senki_" + index + ".srt");
            Matcher m = SUB.matcher(content);
            String newContent = "";
            for (; m.find(); ) {
                String subNumber = m.group(1);
                Date subStartTime = df.parse(m.group(2));
                Date subEndTime = df.parse(m.group(3));
                String subText = m.group(4);
                newContent += subNumber + "\r\n" + df.format(shift(subStartTime, shiftTimeMillis)) + " --> " + df.format(shift(subEndTime, shiftTimeMillis)) + "\r\n" + subText + "\r\n";
            }
            writeFile("C:/Programs/Video/Seikai no Saga/Banner of the Stars I/subs/Seikai_no_Senki_" + index + ".2.srt", newContent);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private String readFile(String path) throws IOException {
        String ret = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "Cp1251"));
        for (String s = in.readLine(); s != null; s = in.readLine()) {
            ret += s + "\r\n";
        }
        in.close();
        return ret;
    }

    private void writeFile(String path, String content) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path), "Cp1251");
        out.write(content);
        out.close();
    }

    private Date shift(Date date, int shiftTimeMillis) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.MILLISECOND, shiftTimeMillis);
        return c.getTime();
    }
    
}
