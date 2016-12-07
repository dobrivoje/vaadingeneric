package remoteexec.beans;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import remoteexec.interfaces.IRemoteShellExecute;

/**
 *
 * @author д06ри
 */
@Component
public class RSHBean implements IRemoteShellExecute {

    @Override
    public String execute(String username, String password, String host, String shellCommand, int timeout) throws JSchException, IOException {
        String rez = "";

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, 22);
        session.setTimeout(timeout < 1 ? 20000 : timeout);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");

        Logger.getLogger("***").log(Level.INFO, "Server [{0}] connecting...", host);
        session.connect();

        Logger.getLogger("***").log(Level.INFO, "Server [{0}] connected.", host);

        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(shellCommand);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                rez = new String(tmp, 0, i);
            }
            if (channel.isClosed()) {
                Logger.getLogger("***").log(Level.INFO, "Exist status : [{0}]", channel.getExitStatus());
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                rez = e.toString();
            }
        }
        Logger.getLogger("***").log(Level.INFO, "Command executed. ", host);

        channel.disconnect();
        Logger.getLogger("***").log(Level.INFO, "Channel disconnected...", host);

        session.disconnect();
        Logger.getLogger("***").log(Level.INFO, "Session disconnected...", host);
        Logger.getLogger("***").log(Level.INFO, "Server [{0}] disconnected...", host);

        return rez;
    }

}
