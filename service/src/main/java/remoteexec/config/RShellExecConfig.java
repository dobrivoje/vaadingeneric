package remoteexec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import remoteexec.beans.RSHBean;
import remoteexec.interfaces.IRemoteShellExecute;

/**
 *
 * @author д06ри
 */
@Configuration
@EnableTransactionManagement
@ComponentScan
public class RShellExecConfig {

    @Bean
    public IRemoteShellExecute getRemoteCommandExecute() {
        return new RSHBean();
    }

}
