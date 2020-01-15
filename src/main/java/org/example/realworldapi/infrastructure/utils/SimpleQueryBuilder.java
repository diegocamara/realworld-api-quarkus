package org.example.realworldapi.infrastructure.utils;

import java.util.LinkedList;
import java.util.List;

public class SimpleQueryBuilder {

  private List<String> queryStatements;
  private List<String> whereStatements;

  public SimpleQueryBuilder() {
    this.queryStatements = new LinkedList<>();
    this.whereStatements = new LinkedList<>();
  }

  public void addQueryStatement(String queryStatement) {
    this.queryStatements.add(queryStatement);
  }

  public void updateQueryStatementConditional(
      boolean updateCondition,
      String queryStatement,
      String whereStatement,
      Runnable afterAddStatements) {
    if (updateCondition) {
      queryStatements.add(queryStatement);
      whereStatements.add(whereStatement);
      afterAddStatements.run();
    }
  }

  public String toQueryString() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(String.join(" ", queryStatements));
    if (!whereStatements.isEmpty()) {
      queryBuilder.append(" where ");
      queryBuilder.append(String.join(" and ", whereStatements));
    }
    return queryBuilder.toString();
  }
}
