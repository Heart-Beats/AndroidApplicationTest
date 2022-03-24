package com.hl.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ${xyZhang} on 2018/3/27.
 */

public class SignUtil {

    private static final String privateKeyStrDebug =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAI+NP4EnxSThNJrK" +
                    "GF/UT6yCp2KbrnKt8eIC5lc3tljNV3+Q4ocrQIdhefILKm7mjS5e12tbFdSpOyVx" +
                    "NNOiiCyIq9ZCqDOyREGvG3B38IcFtnswxvRQEIkWAJhYGl25c9XysB8kLT2HKp8I" +
                    "tZfvFmQq6rbgl839KUORfQtJpOmnAgMBAAECgYA5VfFHZNsZH9oi+RVDMUmhXY39" +
                    "lLDYnSE/ZGkb7OSoE1wfV38i/eOILybFxi/sm6lIHBluh7LwfadiDRVMcl5w0XnM" +
                    "q4P3KxhaIbrXYH2VwryHh3hK0O0ni/4dV8UqS+4aq/U0KaFVAkJaWzlAXuilSjfg" +
                    "iWFVQBGLda/fo7lgAQJBANBsLYY7ekbMVxJE1O7BS8M3LkBDTzXq/XnpVI4Ji/lL" +
                    "YI7p2znMQzSbXm0ck69qcQmJ5+CyG/nUVsPCaM5o8ecCQQCwUiRNeYeDGfpRhe6D" +
                    "+N8/MFWpyZGbyjwvcF3dIbLp2xffjP4jIIWNyw9/PVzkgzlcjhUXCbLwLMDrMVmu" +
                    "8NJBAkEAmSm2NrvEuvx1TC6bEC2qYHZPndgZALWEuq9WfN7VJ0lWVw/SE+JgzOsU" +
                    "dDiY60LceLcIWN5qoqiBbnCSloUWpwJBAIMweuZSOCuploA1wKPc9m5TjklIOtjJ" +
                    "fkJlvtEbk71AUOD6TNTiC7zWi29GvukfYfylkreTddoR0nCsRWhOsoECQQCkzjby" +
                    "TA1zWPK28r662H83Dg5tm7BgTgUCfx9gQtT/ur+FGA8UMhvzCsgoeyXLQLZsILY+" +
                    "epsZeeUGVsYtNrDf";

    private static final String privateKeyStrRelease = "MIICXQIBAAKBgQDCEqHqffgjxFJFSLS756Z2GqLwqwQdkBfLjg0do2jaRgtGqQeZ" +
            "ym95Z1z0K46e4wLsk+qPFqQ0Y7SBvuZ45y2v3tYSARe0sLDi7c3XJpgWdMz9Pm/C" +
            "GR+mcXqiJJwIYMkTyKow4FA6YivScYlVw9Gpwbfh7J4S/0M/uyTsyQgwGQIDAQAB" +
            "AoGAM0xPhjcT3whmoq+sLjr2EiPKTAk4RkQvNfvvVZU7Au8ezeeh/bG3s4Rx2oyk" +
            "3zSsMjKK/pWCg+KS9PSr4OtidLLjWi2gLzHhU7MHmJ97ISpcg93Wmj0QqmX1hjYs" +
            "5oirVloamZwMVQzgr3+THAK3QPapdg/tLBScRcrtgvR2nPECQQD257qQTCP+2O2w" +
            "s8AmS8Z+yS3REnAc86YVjKnd8ZpwewzORlbNPRK5E6aKx+4/XRNPQG4URng2U/Fl" +
            "IjHuqJaNAkEAyTi0JMG9j1RDJKtgvhEK+EfYUYBBINKlgl5PWkoQ7pty+DBW/5zt" +
            "3EoZ1rojwz5QC7kwd12APysuP/jcIi+yvQJADG62sjVjSp3ZuiAS+Nqv8AppSDHJ" +
            "e2u5XTRsCUT1JdzMVEaucD83BiK7LBnNIVuqcT8pS9QQkYvAh/sk0IOksQJBAIry" +
            "2/pxynsZ3Y+JxDEbRa7ytZ556P9IVj/y7erUMGMD9t0JTFoQPHQcu0D6ok1lTxL5" +
            "W01hxHhaUgKczy3QMCUCQQCOSSVQGU7N9cvI5urZ/IvcXrzlB5KKdFNLUVzlcM9P" +
            "HRooQ3RkRzG7D8yYgZFrWrJJnEvm261/omM0VXw178QT";


    public SignUtil() {
    }

    public static String sha1WithRsaSign(String src, @Nullable String signKey, boolean isRelease) {
        String privateKeyStr = privateKeyStrDebug;
        if (signKey != null) {
            privateKeyStr = signKey;
        } else {
            if (isRelease) {
                privateKeyStr = privateKeyStrRelease;
            }
        }

        byte[] encodedPrivateKey = Base64.decode(privateKeyStr, Base64.DEFAULT);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            try {
                factory = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException ex) {
            }
        }
        if(factory == null){
            return "";
        }
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            return ByteUtils.bytes2HexString(signature.sign());
        } catch (InvalidKeySpecException | InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private byte[] reAssmbleData(Map<String, String> map, String md5_key) {
//        Map<String, String> map = map;
//        map.remove("sign");
//        map.remove("sign_format");
//        map.remove("sign_type");

        TreeMap<String, String> params = new TreeMap<String, String>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null &&! entry.getValue().isEmpty()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }

        removeEmpty(params);
        StringBuffer sbtr = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sbtr.append(entry.getKey());
            sbtr.append("=");
            sbtr.append(entry.getValue());
            sbtr.append("&");
        }
        String reAssembleData1 = sbtr.substring(0, sbtr.length() - 1);
        if (md5_key != null) {
            reAssembleData1 = reAssembleData1 + "&key=" + md5_key;//
        }
        Log.e("reAssmbleData", "reAssembleData:" + reAssembleData1);

        return reAssembleData1.getBytes();
    }

    protected void removeEmpty(Map<String, String> map) {
        for (String key : map.keySet()) {
            if (TextUtils.isEmpty(map.get(key))) {
                map.remove(key);
                removeEmpty(map);
                return;
            }
        }
    }

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getBytesMD5(byte[] bytes, boolean isUpterCase) {
        try {

            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(bytes);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            if (isUpterCase) {
                return new String(str).toUpperCase();
            } else {
                return new String(str);
            }

            //返回大写加密值
            // return new String(str).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMD5Signature(String md5_key, Map<String, String> map) {
        byte[] data = reAssmbleData(map, md5_key);

        String value = getBytesMD5(data, true);
        Log.i("getMD5Signature", "md5Sign=" + value);
        return value;

    }
}
