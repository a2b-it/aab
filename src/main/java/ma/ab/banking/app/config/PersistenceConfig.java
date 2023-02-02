/**
 * 
 */
package ma.ab.banking.app.config;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.TypeAlias;

/**
 * @author a.bouabidi
 *
 */
@Configuration
@MapperScan(basePackages = {"ma.ab.banking.app.dao"})
@TypeAlias("ma.ab.banking.app.entity")
public class PersistenceConfig {
	@Bean("timeSpendInterceptor")
    public Interceptor interceptor2(){
        return new TimeSpendInterceptor();
    }
}
