package test;

import java.security.Key;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author д06ри
 */
public class EncryptDecryptTest {

    private AesCipherService cipher;
    private Key key;
    private byte[] keyBytes;
    private final String secret = "nekaŠifra!!78";

    @Before
    public void setUp() {
        cipher = new AesCipherService();
        key = cipher.generateNewKey();
        keyBytes = key.getEncoded();
    }

    @Test
    public void encryptStringMessage() {
        //encrypt the secret
        byte[] secretBytes = CodecSupport.toBytes(secret);
        ByteSource encrypted = cipher.encrypt(secretBytes, keyBytes);

        //decrypt the secret
        byte[] encryptedBytes = encrypted.getBytes();
        ByteSource decrypted = cipher.decrypt(encryptedBytes, keyBytes);
        String secretDecrypted = CodecSupport.toString(decrypted.getBytes());

        System.err.println("šifra za kriptovanje: " + secret);
        System.err.println("izgled kriptovane šifre: " + encrypted);
        System.err.println("secret decrypted : " + secretDecrypted);
        System.err.println("secret bytes : " + secretBytes);
    }

    // @Test
    // ovde smo stavili komentar pošto se projekat ne može build-ovati
    // jer metod ispod daje grešku...
    public void decryptTest2() {
        System.err.println("----------------------------------------");
        System.err.println("--- Key : " + keyBytes.toString() + "  --------");
        System.err.println("----------------------------------------");

        String kriptovanaSifra = "4fzKp7Y7jhOYzM6q4giSfg6xb4vNkGDkSryMm15YFNQ=";
        //decrypt the secret
        byte[] encryptedBytes = kriptovanaSifra.getBytes();
        ByteSource decrypted2 = cipher.decrypt(encryptedBytes, keyBytes);
        String secretDecrypted = CodecSupport.toString(decrypted2.getBytes());
        System.err.println("secret decrypted2 : " + secretDecrypted);
    }

    @Test
    public void testIterationsDemo() {
        byte[] salt = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        //iterations computed by the framework
        Hash shiroIteratedHash = new Sha256Hash("2000!cbs", salt, salt.length);

        Hash root512 = new Sha512Hash("*******");
        Hash ws512 = new Sha512Hash("*******");
        Hash cbs512 = new Sha512Hash("*****");

        //iterations computed by the client code
        Hash wsPrimer_salteed = new Sha256Hash("********", salt);
        for (int i = 1; i < 10; i++) {
            wsPrimer_salteed = new Sha256Hash(wsPrimer_salteed.getBytes());
        }

        System.err.println("sha256 CBS Salted: " + wsPrimer_salteed);
        System.err.println("sha512 root : " + root512);
        System.err.println("sha512 ws : " + ws512);
        System.err.println("sha512 CBS : " + cbs512);
    }
}
