package encryption.config;

import encryption.beans.EncryptionBean;
import encryption.beans.MassEncryptionBean;
import encryption.interfaces.IEncryption;
import encryption.interfaces.IMassEncryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author д06ри
 */
@Configuration
@ComponentScan
public class EncryptionConfig {

    @Bean
    public IEncryption geEB() {
        return new EncryptionBean();
    }

    @Bean
    public IMassEncryption geME() {
        return new MassEncryptionBean(geEB());
    }

}
