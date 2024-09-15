package org.example.realworldapi;

import org.hibernate.dialect.H2Dialect;

public class H2CustomDialect extends H2Dialect {

  public H2CustomDialect() {
    super();
    //    registerColumnType(Types.BINARY, "varbinary");
  }
}
