//Programmed by Ali To request help, contact me on Telegram: @ali_r_1997

//AES-256 Encrypt File with Chipher

package com.fileencryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class FileEncryption {
    private static final String TAG = "FileEncryption";
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final int IV_LENGTH_BYTE = 16;

    public static boolean encryptFile(String inputFilepath, String outputFilepath, String password) {
        try {
            byte[] key = generateKey(password);
            SecretKeySpec secretKey = new SecretKeySpec(key, SECRET_KEY_ALGORITHM);
            IvParameterSpec iv = generateIv();

            FileInputStream inputStream = new FileInputStream(inputFilepath);
            FileOutputStream outputStream = new FileOutputStream(outputFilepath);

            outputStream.write(iv.getIV());

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, read);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] output = cipher.doFinal();
            if (output != null) {
                outputStream.write(output);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error while encrypting file: " + e.getMessage());
            return false;
        }
    }

    public static boolean decryptFile(String inputFilepath, String outputFilepath, String password) {
        try {
            byte[] key = generateKey(password);
            SecretKeySpec secretKey = new SecretKeySpec(key, SECRET_KEY_ALGORITHM);

            FileInputStream inputStream = new FileInputStream(inputFilepath);
            FileOutputStream outputStream = new FileOutputStream(outputFilepath);

            byte[] ivBytes = new byte[IV_LENGTH_BYTE];
            inputStream.read(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, read);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] output = cipher.doFinal();
            if (output != null) {
                outputStream.write(output);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error while decrypting file: " + e.getMessage());
            return false;
        }
    }

    private static byte[] generateKey(String password) throws Exception {
        byte[] passwordBytes = password.getBytes("UTF-8");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] key = sha256.digest(passwordBytes);
        return key;
    }

    private static IvParameterSpec generateIv() {
       SecureRandom random = new SecureRandom();
       byte[] iv = new byte[IV_LENGTH_BYTE];
       random.nextBytes(iv);
       IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
       return ivParameterSpec;
    }

}