package test;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.junit.Before;
import org.junit.Test;
import org.superbapps.auth.App_AccessControl;
import org.superbapps.auth.IAccessAuthControl;
import org.superbapps.auth.roles.Roles;

/**
 *
 * @author д06ри
 */
public class test {

    private final IAccessAuthControl IAC = new App_AccessControl();

    private AesCipherService cipher;
    private Key kljuc;
    private byte[] kljucBajtovi;

    @Before
    public void setUp() {
        cipher = new AesCipherService();
        kljuc = cipher.generateNewKey();
        kljucBajtovi = kljuc.getEncoded();
    }

    @Test()
    public void test() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            String un1 = "ws";
            String un2 = "cbs";
            Subject s1 = IAC.generateSubject();
            Subject s2 = IAC.generateSubject();
            IAC.login(s1, un1, "...");
            IAC.login(s2, un2, "...");

            System.err.println("Is [" + un1 + "] authenticated ? ["
                    + (IAC.authenticated(s1) ? "YES" : "NO !") + "]");
            System.err.println("Is [cbs] authenticated ? ["
                    + (IAC.authenticated(s2) ? "YES" : "NO !") + "]");

            System.err.println("Test 1: ROLES :");
            System.err.println(Roles.getRoles());

            System.err.println("-----------------Roles--------------------------");

            for (Roles r : Roles.values()) {
                if (IAC.hasRole(s1, r)) {
                    System.err.println(s1.getPrincipal() + " HAS ROLE : " + r);
                }

                if (IAC.hasRole(s2, r)) {
                    System.err.println(s2.getPrincipal() + " HAS ROLE : " + r);
                }

            }

            System.err.println("--------------Permissions------------------------");
            System.err.println("P: " + Roles.getPermissions());

            for (Roles p : Roles.values()) {
                if (IAC.isPermitted(s1, p)) {
                    System.err.println(s1.getPrincipal() + " IS PERMITTED : " + p.name());
                }
                if (IAC.isPermitted(s2, p)) {
                    System.err.println(s2.getPrincipal() + " IS PERMITTED : " + p.name());
                }
            }

        } catch (Exception e) {
            System.err.println("greška!");
            System.err.println(e.toString());
        }
    }

    // @Test
    public void sifriraj() {
        String poruka = "Dobrivoje Prtenjak. Milić Momčilo i bakuta Zaga Milatović";
        // String kljucMoj = "dedaMocika20011930###";

        byte[] kljucBajtovi = this.kljucBajtovi;
        ByteSource bs = ByteSource.Util.bytes(poruka);
        byte[] porukaBajtovi = bs.getBytes();

        ByteSource sifrovanoByteSource = cipher.encrypt(porukaBajtovi, kljucBajtovi);
        String sifrovanaPoruka = sifrovanoByteSource.toBase64();
        System.err.println(">>>> ključ1 : " + Base64.encode(kljucBajtovi));
        System.err.println(">>>> šifrovano1 : " + sifrovanaPoruka);
        System.err.println("-----------------------------------------------");
    }

    @Test
    public void desifrovanje1() {
        String kriptovanaPoruka = "FJNPRHCdQJd8EZ9Ak8T09wconPtI68HSzd+ASg4E4VE=";
        String kljucDekriptStr = "Bc3bpLgbEOXTKWTVhgDhvQ==";
        byte[] kriptovanaPorukaBytes = Base64.decode(kriptovanaPoruka);
        byte[] kriptovaniKljucBytes = Base64.decode(kljucDekriptStr);

        ByteSource krPorByteSource = cipher.decrypt(kriptovanaPorukaBytes, kriptovaniKljucBytes);

        String poruka2 = CodecSupport.toString(krPorByteSource.getBytes());
        System.err.println("<<<< dešifrovanje1 : " + poruka2);
    }

    // @Test
    public void desifrovanje2() {
        String kriptovanaPoruka = "+c9jtCU15oBiNDz2PdcNuTol2RLS/JtJuE89bhQb2z8NYMJ10fBgYx1x0PwggwMjJS06JvmdqeNtdoHR263fj460n9pzCMWU/GsA6dOFjpQ=";
        String kljucDekriptStr = "hS9faKYuRZ4Fxg/gh8F1Kg==";
        byte[] kriptovanaPorukaBytes = Base64.decode(kriptovanaPoruka);
        byte[] kriptovaniKljucBytes = Base64.decode(kljucDekriptStr);

        ByteSource krPorByteSource = cipher.decrypt(kriptovanaPorukaBytes, kriptovaniKljucBytes);

        String poruka2 = CodecSupport.toString(krPorByteSource.getBytes());
        System.err.println("<<<< dešifrovanje2 : " + poruka2);
    }

}
