package test;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import remoteexec.config.RShellExecConfig;
import remoteexec.interfaces.IRemoteShellExecute;

public class test_RSHell {

    ApplicationContext context;
    IRemoteShellExecute rshexec;
    String command;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(RShellExecConfig.class);
        rshexec = context.getBean(IRemoteShellExecute.class);

        // command = "touch /tmp/test.file.txt";
        // command = "/etc/init.d/postgresql restart";
        // command = "/etc/init.d/postgresql stop";
        // command = "/etc/init.d/postgresql start";
        command = "sh /tmp/dobriSkript.sh";
    }

    @Test
    public void shellCommandTest() {
        try {
            System.out.println(rshexec.execute("root", "*********", "188.40.85.67", command, 2000));
        } catch (JSchException | IOException ex) {
            Logger.getLogger("***").log(Level.SEVERE, ex.getMessage());
        }
    }

}
