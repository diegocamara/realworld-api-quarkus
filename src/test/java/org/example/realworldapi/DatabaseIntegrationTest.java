package org.example.realworldapi;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class DatabaseIntegrationTest {

  public static EntityManagerFactory entityManagerFactory;
  public static EntityManager entityManager;
  private static DataSource dataSource;
  private static Set<String> entities;

  static {
    entities = new HashSet<>();
    dataSource = dataSource();
    entityManagerFactory = sessionFactory();
    entityManager = entityManagerFactory.createEntityManager();
  }

  private static SessionFactory sessionFactory() {
    ServiceRegistry serviceRegistry = null;
    SessionFactory sessionFactory = null;
    try {
      Configuration configuration = configuration();
      StandardServiceRegistryBuilder standardServiceRegistryBuilder =
          new StandardServiceRegistryBuilder();
      standardServiceRegistryBuilder.applySettings(configuration.getProperties());
      serviceRegistry = standardServiceRegistryBuilder.build();
      sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    } catch (Exception ex) {
      ex.printStackTrace();
      StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }
    return sessionFactory;
  }

  private static Configuration configuration() {
    Configuration configuration = new Configuration();
    configuration.setProperties(properties());
    configEntityClasses(configuration, "org.example.realworldapi");
    return configuration;
  }

  private static Properties properties() {
    Properties properties = new Properties();
    properties.put(Environment.DRIVER, "org.h2.Driver");
    properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
    properties.put(Environment.SHOW_SQL, false);
    properties.put(Environment.FORMAT_SQL, true);
    properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
    properties.put(Environment.HBM2DDL_AUTO, "create-drop");
    properties.put(Environment.DATASOURCE, dataSource);
    return properties;
  }

  private static void configEntityClasses(Configuration configuration, String packageToScan) {
    Reflections reflections = new Reflections(packageToScan);
    reflections
        .getTypesAnnotatedWith(Entity.class)
        .forEach(
            entity -> {
              String tableName = entity.getAnnotation(Table.class).name();
              entities.add(tableName);
              configuration.addAnnotatedClass(entity);
            });
  }

  private static DataSource dataSource() {
    JdbcDataSource jdbcDataSource = new JdbcDataSource();
    jdbcDataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
    jdbcDataSource.setUser("sa");
    jdbcDataSource.setPassword("");
    return jdbcDataSource;
  }

  public void clear() {
    transaction(
        () ->
            entities.forEach(
                tableName ->
                    entityManager
                        .createNativeQuery(
                            "SET FOREIGN_KEY_CHECKS = 0; DELETE FROM "
                                + tableName
                                + "; SET FOREIGN_KEY_CHECKS = 1;")
                        .executeUpdate()));
  }

  public void transaction(Runnable command) {
    entityManager.getTransaction().begin();
    entityManager.flush();
    command.run();
    entityManager.getTransaction().commit();
  }

  public <T> T transaction(TransactionRunnable<T> command) {
    entityManager.getTransaction().begin();
    entityManager.clear();
    T result = command.run();
    entityManager.getTransaction().commit();
    return result;
  }

  public interface TransactionRunnable<T> {
    T run();
  }
}
