package com.bitlake.backend.configuration;

import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class JooqConfiguration {

  @Bean
  public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
    return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
  }

  @Bean
  public DefaultDSLContext dsl(org.jooq.Configuration config) {
    return new DefaultDSLContext(config);
  }

  @Bean
  public DefaultConfiguration configuration(DataSourceConnectionProvider connectionProvider) {
    DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
    jooqConfiguration.set(connectionProvider);
    return jooqConfiguration;
  }
}
