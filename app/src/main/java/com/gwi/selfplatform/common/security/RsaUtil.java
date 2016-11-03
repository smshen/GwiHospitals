package com.gwi.selfplatform.common.security;

import com.gwi.selfplatform.common.utils.Logger;

import org.kobjects.base64.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * 
 * @author 彭毅
 *
 */
public class RsaUtil {

    private static final String MODULES = "1kzfS1LPujYaYooL9jSm8D3Wvd0FCqbsrlvfwOBAXLGmDJYQ/NJE7Tk2BwJubzhFku9sbUEAWegMdoOSIqd9rw6O1yXQWyVIQ+o70olgpqt72kwy9+D86owwiukD7EjsOpgqj7dRSXkcMGHu5Aq7ogN8DEZ7qHv3igaLh4uZz58=";

    private static final String COMPONETS = "AQAB";

    /**
     * 使用给定的MODULES和COMPONETS加密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        RSAPublicKey theKey = getPublicKey(Base64.decode(MODULES),
                Base64.decode(COMPONETS));
        return encryptByPublicKey(data, theKey);
    }

    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encode(cipher.doFinal(data.getBytes("UTF-8")));
    }

    public static RSAPublicKey getPublicKey(byte[] modulus, byte[] exponent) {
        try {
            BigInteger b1 = new BigInteger(1, modulus);
            BigInteger b2 = new BigInteger(1, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            Logger.e("RsaUtil", "getPublicKey()", e);
            return null;
        }
    }

    public static void main(String[] args)throws Exception{
        String data = "12345678";
        System.out.println(encrypt(data));
    }
}
