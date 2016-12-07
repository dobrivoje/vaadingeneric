package encryption.interfaces;

/**
 * @author д06ри
 */
public interface IEncryption {

    String encrypt(String key, String value);

    String decrypt(String key, String encrypted);
}
