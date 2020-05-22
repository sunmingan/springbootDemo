package org.example.sun.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@MapperScan(basePackages = "org.example.sun.mapper",sqlSessionFactoryRef = MybatisConfiguration.DEFAULT_SESSION_FACTORY_NAME)
public class MybatisConfiguration {

    public static final String DEFAULT_DATASOURCE_NAME = "defaultDs";

    public static final String DEFAULT_SESSION_FACTORY_NAME = "defaultSessionFactory";

    public static final String DEFAULT_JDBC_TEMPLATE_NAME = "defaultJdbcTemplate";

    public static final String MYBATIS_TRANSACTION_NAME = "defaultTranscation";

    @Value("${spring.datasource.driverClassName}")
    private String driverName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.hikari.connection-test-query}")
    private String testQuery;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maxPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minIdle;

    @Value("${spring.datasource.hikari.validation-timeout}")
    private long validationTimeout;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private long idleTimeout;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.max-lifetime}")
    private long maxLifetime;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Bean(name = DEFAULT_DATASOURCE_NAME)
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTestQuery(testQuery);
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setValidationTimeout(validationTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionInitSql("set names utf8mb4;");//兼容utf8mb4 2019-6-10
        return new HikariDataSource(config);
    }

    @Bean(name = DEFAULT_SESSION_FACTORY_NAME)
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DEFAULT_DATASOURCE_NAME) DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactory.setVfs(SpringBootVFS.class);

        String[] arr = StringUtils.tokenizeToStringArray(mapperLocations, //
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        Set<String> locations = new HashSet<>(Arrays.asList(arr));

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        for (String location : locations) {
            resources.addAll(Arrays.asList(resolver.getResources(location)));
        }
        sessionFactory.setMapperLocations(resources.toArray(new Resource[resources.size()]));
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        sessionFactory.setConfiguration(configuration);
        return sessionFactory.getObject();
    }

    @Bean(name = DEFAULT_JDBC_TEMPLATE_NAME)
    @Primary
    public JdbcTemplate jdbcTemplate(@Qualifier(DEFAULT_DATASOURCE_NAME) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = MYBATIS_TRANSACTION_NAME)
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier(DEFAULT_DATASOURCE_NAME) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
