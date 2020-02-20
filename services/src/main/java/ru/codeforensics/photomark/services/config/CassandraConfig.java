package ru.codeforensics.photomark.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
@PropertySource("classpath:/cassandra.properties")
public class CassandraConfig extends AbstractCassandraConfiguration {

  @Value("${cassandra.contactpoints}")
  private String contactPoints;
  @Value("${cassandra.port}")
  private int port;
  @Value("${cassandra.keyspace}")
  private String keyspace;

  @Override
  public String getContactPoints() {
    return contactPoints;
  }

  @Override
  protected int getPort() {
    return port;
  }

  @Override
  protected String getKeyspaceName() {
    return keyspace;
  }
}
