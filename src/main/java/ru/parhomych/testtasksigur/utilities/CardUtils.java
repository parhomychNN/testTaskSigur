package ru.parhomych.testtasksigur.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import ru.parhomych.testtasksigur.service.EmployeeService;
import ru.parhomych.testtasksigur.service.GuestService;

import java.util.Random;

public class CardUtils {

    @Autowired
    GuestService guestService;

    @Autowired
    EmployeeService employeeService;

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static byte[] getRandom16ByteArray() {
        Random random = new Random();
        byte[] result = new byte[16];
        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.parseByte(String.valueOf(random.nextInt(255) - 128));
        }
        return result;
    }

    public static String getHexRepresentationOfByteArray(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


}
