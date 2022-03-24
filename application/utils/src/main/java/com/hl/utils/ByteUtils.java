package com.hl.utils;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;

public class ByteUtils {

    /**
     * 十六进制 转换 byte[]
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexString2Bytes(String hexStr) {
        if (hexStr == null)
            return null;
        if (hexStr.length() % 2 != 0) {
            hexStr += "0";
        }
        byte[] data = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            char hc = hexStr.charAt(2 * i);
            char lc = hexStr.charAt(2 * i + 1);
            byte hb = hexChar2Byte(hc);
            byte lb = hexChar2Byte(lc);
            if (hb < 0 || lb < 0) {
                return null;
            }
            int n = hb << 4;
            data[i] = (byte) (n + lb);
        }
        return data;
    }

    public static byte hexChar2Byte(char c) {
        if (c >= '0' && c <= '9')
            return (byte) (c - '0');
        if (c >= 'a' && c <= 'f')
            return (byte) (c - 'a' + 10);
        if (c >= 'A' && c <= 'F')
            return (byte) (c - 'A' + 10);
        return -1;
    }

    /**
     * byte[] 转 16进制字符串
     *
     * @param arr
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String bytes2HexString(byte[] arr) {
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
        }
        return sbd.toString().toUpperCase();
    }

    public static String bytes2HexStringWithSpace(byte[] arr) {
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
            sbd.append(" ");
        }
        return sbd.toString();
    }

    static public String getBCDString(byte[] data, int start, int end) {
        byte[] t = new byte[end - start + 1];
        System.arraycopy(data, start, t, 0, t.length);
        return ByteUtils.bytes2HexString(t);
    }

    static public String getHexString(byte[] data, int start, int end) {
        byte[] t = new byte[end - start + 1];
        System.arraycopy(data, start, t, 0, t.length);
        return ByteUtils.bytes2HexStringWithSpace(t);
    }
    /**
     * int转byte数组[]
     *
     * @param source int值
     * @param len 数组长度
     * @return
     */
    public static byte[] toByteArray(int source, int len) {
        byte[] bLocalArr = new byte[len];
        for (int i = 0; (i < 4) && (i < len); i++) {
            bLocalArr[len - 1 - i] = (byte) (source >> 8 * i & 0xFF);
        }
        return bLocalArr;
    }
    /**
     * byte[]转int
     *
     * @param bRefArr
     * @return
     */
    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;
        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    /**
     * String转为ASCII字符
     * 0  --  48
     * 1  --  49
     * 以此类推，如2016返回的则是 50484954
     * @param value
     * @return
     */
    public static String string2Ascii(String value){
        if (null == value) return null;
        StringBuffer sbu = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            sbu.append((int)value.toCharArray()[i]);
        }
        return sbu.toString();
     }

    public static byte[] ascii2Bcd(String ascii) {
        if (ascii == null)
            return null;
        if ((ascii.length() & 0x01) == 1)
            ascii = "0" + ascii;
        byte[] asc = ascii.getBytes();
        byte[] bcd = new byte[ascii.length() >> 1];
        for (int i = 0; i < bcd.length; i++) {
            bcd[i] = (byte)(hex2byte((char)asc[2 * i]) << 4 | hex2byte((char)asc[2 * i + 1]));
        }
        return bcd;
    }

    /**
     * 快速预授权，右靠
     * @param ascii
     * @return
     */
    public static byte[] ascii2BcdRight(String ascii) {
        if (ascii == null)
            return null;
        if ((ascii.length() & 0x01) == 1)
            ascii = ascii+"0";
        byte[] asc = ascii.getBytes();
        byte[] bcd = new byte[ascii.length() >> 1];
        for (int i = 0; i < bcd.length; i++) {
            bcd[i] = (byte)(hex2byte((char)asc[2 * i]) << 4 | hex2byte((char)asc[2 * i + 1]));
        }
        return bcd;
    }

    public static String bcd2Ascii(final byte[] bcd) {
        if (bcd == null)
            return "";
        StringBuilder sb = new StringBuilder(bcd.length << 1);
        for (byte ch : bcd) {
            // bcd码无负数，负数高位补1不正确，应该无符号拓展
            // >>>只对32bit和64bit有效？changed by eric 20160331
            byte half = (byte) ((ch >> 4) & 0x0f);
            sb.append((char)(half + ((half > 9) ? ('A' - 10) : '0')));
            half = (byte) (ch & 0x0f);
            sb.append((char)(half + ((half > 9) ? ('A' - 10) : '0')));
        }
        return sb.toString();
    }

    public static byte hex2byte(char hex) {
        if (hex <= 'f' && hex >= 'a') {
            return (byte) (hex - 'a' + 10);
        }

        if (hex <= 'F' && hex >= 'A') {
            return (byte) (hex - 'A' + 10);
        }

        if (hex <= '9' && hex >= '0') {
            return (byte) (hex - '0');
        }

        return 0;
    }

    public static int bytes2Int(byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }

        int total = 0;
        for (int i = 0; i < data.length; i++) {
            total += (data[i] & 0xff) << (data.length - i - 1) * 8;
        }
        return total;
    }
    public static byte[] toBytes(String data, String charsetName) {
        try {
            return data.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] toBytes(String data) {
        return toBytes(data, "ISO-8859-1");
    }

    public static byte[] toGBK(String data) {
        return toBytes(data, "GBK");
    }

    public static byte[] toGB2312(String data) {
        return toBytes(data, "GB2312");
    }

    public static byte[] toUtf8(String data) {
        return toBytes(data, "UTF-8");
    }

    public static String fromBytes(byte[] data, String charsetName) {
        try {
            if (data == null) {
                return "null";
            }
            return new String(data, charsetName);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String fromBytes(byte[] data) {
        return fromBytes(data, "ISO-8859-1");
    }

    public static String fromGBK(byte[] data) {
        return fromBytes(data, "GBK");
    }

    public static String fromGB2312(byte[] data) {
        return fromBytes(data, "GB2312");
    }

    public static String fromUtf8(byte[] data) {
        return fromBytes(data, "UTF-8");
    }

    /**
     * @param msg
     * @param bytes
     */
    public static void dumpHex(String msg, byte[] bytes) {
        if (null == bytes) {
            System.out.println("bytes is null\n");
            return;
        }
        int length = bytes.length;
        msg = (msg == null) ? "" : msg;
        System.out.printf("-------------------------- " + msg + "(len:%d) --------------------------\n", length);
        for (int i = 0; i < bytes.length; i++) {
            if (i % 16 == 0) {
                if (i != 0) System.out.println();
                System.out.printf("0x%08X    ", i);
            }
            System.out.printf("%02X ", bytes[i]);
        }
        System.out.println();
    }

//    public static void main(String[] args) {
//        byte[] bts= new byte[2];
//        bts[0] = 0x0F;
//        System.out.println(toInt(bts));
//    }
}