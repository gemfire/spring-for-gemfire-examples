package io.vmware.event;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.CacheFactory;

public class ConnectionPool {
  private final HikariDataSource dataSource;
  private HikariConfig config;

  private static LogWriter log;

  static {
    log = CacheFactory.getAnyInstance().getDistributedSystem().getLogWriter();
  }

  private ConnectionPool() {
    String userName = System.getProperty("postgres.username");
    String password = System.getProperty("postgres.password");
    String url = "jdbc:postgresql:postgres";

    log.info("username: "+ userName);
    log.info("password: "+ password);

    config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setUsername(userName);
    config.setPassword(password);
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    dataSource = new HikariDataSource(config);
  }
  private static ConnectionPool connectionPool = new ConnectionPool();

  public static ConnectionPool getInstance() {
    return connectionPool;
  }

  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
