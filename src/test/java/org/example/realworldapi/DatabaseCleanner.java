package org.example.realworldapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseCleanner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;
    private Connection connection;
    private Statement statement;

    public DatabaseCleanner(DataSource dataSource) {
        try {
            this.dataSource = dataSource;
            this.connection = this.dataSource.getConnection();
            this.statement = buildSqlStatement(tableNames());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void clear(){
        try{
            logger.info("Cleaning up data");
            this.statement.executeBatch();
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    private List<String> tableNames() throws SQLException {
        List<String> tableNames = new LinkedList<>();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(connection.getCatalog(), null, null, new String[]{"TABLE"});
        while(resultSet.next()){
            tableNames.add(resultSet.getString("TABLE_NAME"));
        }
        return tableNames;
    }

    private Statement buildSqlStatement(List<String> tableNames) throws SQLException {
        Statement statement = connection.createStatement();
        statement.addBatch(sql("SET FOREIGN_KEY_CHECKS = 0"));
        addDeleteSatements(tableNames, statement);
        statement.addBatch(sql("SET FOREIGN_KEY_CHECKS = 1"));
        return statement;
    }

    private void addDeleteSatements(List<String> tableNames, Statement statement) {
        tableNames.forEach(tableName -> {
            try {
                statement.addBatch(sql("DELETE FROM " + tableName));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String sql(String sql) {
        logger.info("Registering SQL: {}", sql);
        return sql;
    }

}
