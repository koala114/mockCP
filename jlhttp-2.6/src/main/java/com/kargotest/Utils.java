package com.kargotest;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Utils {
    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static String decryptAESKey(String encryptedAESKey, PrivateKey key){
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedAESKeyBytes = Base64.getDecoder().decode(encryptedAESKey.getBytes());
            byte[] decryptedAESKeyBytes = cipher.doFinal(encryptedAESKeyBytes);
            String decryptedAESKey = new String(decryptedAESKeyBytes, StandardCharsets.UTF_8);
            return decryptedAESKey;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String encryptedMessage(String input, String aeskey,
                                 String iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        SecretKey secretKey = new SecretKeySpec(aeskey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decryptedMessage(String content, String key, String offset) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(offset.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(content)), "UTF-8");
    }
}
