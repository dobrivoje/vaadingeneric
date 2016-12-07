package remoteexec.interfaces;

import com.jcraft.jsch.JSchException;
import java.io.IOException;

/**
 * @author д06ри
 */
public interface IRemoteShellExecute {

    /**
     * Izvršavanje komande na udaljenom Linux serveru.
     *
     * @param username, Username
     * @param password Password
     * @param host DNS adresa servera
     * @param shellCommand Komanda koju treba izvršiti.
     * @param timeout Timeout u milisekundama. -1: timeout = 20 sekundi.
     * @return Odgovor od servera.
     * @throws com.jcraft.jsch.JSchException
     * @throws java.io.IOException
     */
    String execute(String username, String password, String host, String shellCommand, int timeout) throws JSchException, IOException;
}
