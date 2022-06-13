package org.example.realworldapi;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

public class H2CustomDialect extends H2Dialect {

  public H2CustomDialect() {
    super();
    registerColumnType(Types.BINARY, "varbinary");
  }
}
