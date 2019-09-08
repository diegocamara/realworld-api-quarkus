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
import javax.sql.DataSource;
import java.util.Properties;

public class DatabaseIntegrationTest {

    public static EntityManagerFactory entityManagerFactory;
    public static EntityManager entityManager;
    public static DatabaseCleanner databaseCleanner;
    private static DataSource dataSource;

    static {
        dataSource = dataSource();
        entityManagerFactory = sessionFactory();
        entityManager = entityManagerFactory.createEntityManager();
        databaseCleanner = new DatabaseCleanner(dataSource);
    }

    private static SessionFactory sessionFactory() {
        ServiceRegistry serviceRegistry = null;
        SessionFactory sessionFactory = null;
        try{
            Configuration configuration = configuration();
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
            standardServiceRegistryBuilder.applySettings(configuration.getProperties());
            serviceRegistry = standardServiceRegistryBuilder.build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }catch(Exception ex){
            ex.printStackTrace();
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
        return sessionFactory;
    }

    private static Configuration configuration(){
        Configuration configuration = new Configuration();
        configuration.setProperties(properties());
        configEntityClasses(configuration, "");
        return configuration;
    }

    private static Properties properties(){
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.h2.Driver");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "none");
        properties.put(Environment.DATASOURCE, dataSource);
        return properties;
    }

    private static void configEntityClasses(Configuration configuration, String packageToScan){
        Reflections reflections = new Reflections(packageToScan);
        reflections.getTypesAnnotatedWith(Entity.class).forEach(configuration::addAnnotatedClass);
    }

    private static DataSource dataSource() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        jdbcDataSource.setUser("sa");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }

    public void transaction(Runnable command) {
        entityManager.getTransaction().begin();
        command.run();
        entityManager.flush();
        entityManager.getTransaction().commit();
    }



}