package encryption.beans;

import encryption.interfaces.IEncryption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 *
 * @author д06ри
 */
@Component
public class EncryptionBean implements IEncryption {

    private IvParameterSpec iv;
    private SecretKeySpec skeySpec;
    private Cipher cipher;

    @Override
    public String encrypt(String key, String value) {
        try {
            iv = new IvParameterSpec(key.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            Logger.getLogger(EncryptionBean.class.getSimpleName()).log(Level.INFO, "******** EncryptionBean - encrypt success.");

            return Base64.encodeBase64String(encrypted);

        } catch (Exception ex) {
            Logger.getLogger(EncryptionBean.class.getSimpleName()).log(Level.SEVERE, "******** EncryptionBean - encrypt - Error : {0}", ex.getMessage());
        }

        return null;
    }

    @Override
    public String decrypt(String key, String encrypted) {
        try {
            iv = new IvParameterSpec(key.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            Logger.getLogger(EncryptionBean.class.getSimpleName()).log(Level.INFO, "******** EncryptionBean - decrypt success.");

            return new String(original);

        } catch (Exception ex) {
            Logger.getLogger(EncryptionBean.class.getSimpleName()).log(Level.SEVERE, "******** EncryptionBean - decrypt - Error : {0}", ex.getMessage());
        }

        return null;
    }

}
