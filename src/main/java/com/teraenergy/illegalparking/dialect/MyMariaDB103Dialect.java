package com.teraenergy.illegalparking.dialect;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * Date : 2022-09-25
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
public class MyMariaDB103Dialect extends MariaDB103Dialect {

    public MyMariaDB103Dialect() {
        super();
        this.registerFunction( "ASTEXT", new StandardSQLFunction( "ASTEXT", StandardBasicTypes.CHARACTER) );
        this.registerFunction( "POLYGONFROMTEXT", new StandardSQLFunction( "POLYGONFROMTEXT", StandardBasicTypes.CHARACTER) );
    }

}
