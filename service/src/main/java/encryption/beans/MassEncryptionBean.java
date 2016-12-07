package encryption.beans;

import encryption.interfaces.IEncryption;
import encryption.interfaces.IMassEncryption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author д06ри
 */
@Component
public class MassEncryptionBean implements IMassEncryption {

    @Autowired
    private final IEncryption encryption;

    public MassEncryptionBean(IEncryption encryption) {
        this.encryption = encryption;
    }

    @Override
    public List<String> encrypt(String secretKey, List<String> listForEncryption) {
        List<String> L = new ArrayList<>();

        for (String messageToEncrypt : listForEncryption) {
            String encrypted = encryption.encrypt(secretKey, messageToEncrypt);
            if (encrypted != null) {
                L.add(encrypted);
            }
        }

        return L;
    }

    @Override
    public List<String> decrypt(String secretKey, List<String> encryptedList) {
        List<String> L = new ArrayList<>();

        for (String messageToDecrypt : encryptedList) {
            String decrypted = encryption.decrypt(secretKey, messageToDecrypt);
            if (decrypted != null) {
                L.add(decrypted);
            }
        }

        return L;
    }

}
