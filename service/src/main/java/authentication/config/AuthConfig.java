package authentication.config;

import authentication.beans.AuthBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.superbapps.auth.IAccessAuthControl;

/**
 *
 * @author д06ри
 */
@Configuration
@EnableTransactionManagement
@ComponentScan
public class AuthConfig {

    @Bean
    public IAccessAuthControl getRemoteCommandExecute() {
        return new AuthBean();
    }

}
