package playground;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfigDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Profile("dev")
    @Bean(name = "dataSource")
    public DataSource devDataSource() {
        LOGGER.info("dataSource: 'HSQL'");
        return doCreateEmbeddedDataBaseForTest();
    }

    @Profile("prod")
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "app.datasource.druid")
    public DataSource prodDataSource() {
        LOGGER.info("dataSource: 'MYSQL'");
        return new DruidDataSource();
    }

    @Bean(name = "dataSource")
    @ConditionalOnMissingBean(value = DataSource.class)
    public DataSource lastDataSource() {
        LOGGER.warn("dataSource bean NOT found. creating embedded data base data source bean. type={}", "HSQL");
        return doCreateEmbeddedDataBaseForTest();
    }

    private DataSource doCreateEmbeddedDataBaseForTest() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:/META-INF/hsqldb/schema.sql")
                .addScript("classpath:/META-INF/hsqldb/data.sql")
                .build();
    }

}
