package com.oc.hawk.project.domain.model.codebase.git.service;

import com.oc.hawk.project.domain.model.codebase.git.CodeBaseIdentity;
import com.oc.hawk.project.domain.model.project.exception.CodeBasePasswordDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.utils.Utils;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Properties;

@Slf4j
public class AesEncryptStrategy implements GitPasswordEncryptStrategy {
    private final String SALT = "hawk";
    private final String TRANSFORM = "AES/CBC/PKCS5Padding";

//    private final Properties properties = new Properties();
//    public AESEncryptStrategy() {
//        properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CryptoCipherFactory.CipherProvider.OPENSSL.getClassName());
//    }

    private static String asString(ByteBuffer buffer) {
        final ByteBuffer copy = buffer.duplicate();
        final byte[] bytes = new byte[copy.remaining()];
        copy.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Converts String to UTF8 bytes
     *
     * @param input the input string
     * @return UTF8 bytes
     */
    private byte[] getUtf8Bytes(final String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    private SecretKeySpec getSecretKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        /*
         *AES-256
         */
        return new SecretKeySpec(getBytes(key), "AES");
    }

    private byte[] getBytes(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(key.toCharArray(), SALT.getBytes(StandardCharsets.UTF_8), 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }

    @Override
    public CodeBaseIdentity getPlainText(String username, String password) {
        Properties properties = new Properties();
        final ByteBuffer outBuffer;
        final int bufferSize = 1024;
        ByteBuffer decoded = ByteBuffer.allocateDirect(bufferSize);
        //Creates a CryptoCipher instance with the transformation and properties.
        try (CryptoCipher decipher = Utils.getCipherInstance(TRANSFORM, properties)) {
            decipher.init(Cipher.DECRYPT_MODE, getSecretKey(username), new IvParameterSpec(getBytes(username)));
            outBuffer = ByteBuffer.allocateDirect(bufferSize);
            outBuffer.put(Base64Utils.decode(getUtf8Bytes(password)));
            outBuffer.flip();
            decipher.update(outBuffer, decoded);
            decipher.doFinal(outBuffer, decoded);
            decoded.flip(); // ready for use
            log.debug("Code base password decoded={}", asString(decoded));
        } catch (Exception e) {
            throw new CodeBasePasswordDecodeException("无法解密密码", e);
        }

        return new CodeBaseIdentity(username, asString(decoded));

    }

    @Override
    public CodeBaseIdentity getCipherText(String username, String password) {
        try {
            Properties properties = new Properties();
            final ByteBuffer outBuffer;
            final int bufferSize = 1024;
            final int updateBytes;
            final int finalBytes;
            //Creates a CryptoCipher instance with the transformation and properties.
            try (CryptoCipher encipher = Utils.getCipherInstance(TRANSFORM, properties)) {

                ByteBuffer inBuffer = ByteBuffer.allocateDirect(bufferSize);
                outBuffer = ByteBuffer.allocateDirect(bufferSize);
                inBuffer.put(getUtf8Bytes(password));

                inBuffer.flip(); // ready for the cipher to read it
                // Initializes the cipher with ENCRYPT_MODE,key and iv.
                encipher.init(Cipher.ENCRYPT_MODE, getSecretKey(username), new IvParameterSpec(getBytes(username)));

                // Continues a multiple-part encryption/decryption operation for byte buffer.
                updateBytes = encipher.update(inBuffer, outBuffer);

                // We should call do final at the end of encryption/decryption.
                finalBytes = encipher.doFinal(inBuffer, outBuffer);
            }

            outBuffer.flip(); // ready for use as decrypt
            byte[] encoded = new byte[updateBytes + finalBytes];
            outBuffer.duplicate().get(encoded);
            String encodedString = Base64Utils.encodeToString(encoded);
            log.debug("Code base password encode={}", encodedString);
            return new CodeBaseIdentity(username, encodedString);
        } catch (Exception e) {
            throw new CodeBasePasswordDecodeException("无法加密密码", e);
        }
    }


}
