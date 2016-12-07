package encryption.interfaces;

import java.util.List;

/**
 * @author д06ри
 */
public interface IMassEncryption {

    List<String> encrypt(String secretKey, List<String> listForEncryption);

    List<String> decrypt(String key, List<String> encryptedList);
}
