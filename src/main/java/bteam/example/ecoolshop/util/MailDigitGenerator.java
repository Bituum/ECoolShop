package bteam.example.ecoolshop.util;

import java.util.Random;

public class MailDigitGenerator {
    public static String generate() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        int border = 10;

        for (int i = 0; i < 6; i++) {
            stringBuffer.append(random.nextInt(border));
        }

        return stringBuffer.toString();
    }
}
