package com.cldbiz.userportal.unit;

import javax.sql.DataSource;

import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.db2.Db2DataTypeFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@Configuration
@ComponentScan(basePackages = "com.cldbiz")
public class DBUnitConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DBUnitConfig.class);

	@Autowired
	private Environment env;
	
	@Autowired 
	private DataSource dataSource;
	
	
	@Bean
	public DatabaseConfigBean getDatabaseConfig() {
		DatabaseConfigBean dbCfg = new DatabaseConfigBean();
		
		dbCfg.setTableType(new String[] {
			"TABLE", "VIEW"
		});
		dbCfg.setQualifiedTableNames(true);
		// dbCfg.setDatatypeFactory(getDataTypeFactory(env.getProperty("spring.jpa.properties.hibernate.dialect")));
		dbCfg.setDatatypeFactory(getDataTypeFactory(env.getProperty("spring.jpa.database-platform")));
		return dbCfg;
	}
	
	@Bean
	public DatabaseDataSourceConnectionFactoryBean getDatabaseDataSourceConnectionFactory() {
		DatabaseDataSourceConnectionFactoryBean dbDsConnFactory = new DatabaseDataSourceConnectionFactoryBean();
		
		dbDsConnFactory.setDatabaseConfig(getDatabaseConfig());
		dbDsConnFactory.setDataSource(dataSource);
		
		return dbDsConnFactory;
	}
	
	private IDataTypeFactory getDataTypeFactory(String dialect) {
		IDataTypeFactory dataTypeFactory = null;
		switch (dialect) {
			case "org.hibernate.dialect.Oracle10gDialect":
				dataTypeFactory = new Oracle10DataTypeFactory();
				break;
        	case "org.hibernate.dialect.SQLServer2012Dialect":  	
        		dataTypeFactory = new MsSqlDataTypeFactory();
                break;
        	case "org.hibernate.dialect.DB2Dialect":  	
        		dataTypeFactory = new Db2DataTypeFactory();
        		break;
        	case "org.hibernate.dialect.MySQL5Dialect":
        	case "org.hibernate.dialect.MySQLMyISAMDialect":
        	case "org.hibernate.dialect.MySQL5InnoDBDialect":
        		dataTypeFactory = new MySqlDataTypeFactory();
        		break;
        	case "org.hibernate.dialect.PostgreSQLDialect":
        		dataTypeFactory = new PostgresqlDataTypeFactory();
        		break;
        	case "org.hibernate.dialect.HSQLDialect":
        		dataTypeFactory = new HsqldbDataTypeFactory();
        		break;
        	case "org.hibernate.dialect.H2Dialect":  	
        		dataTypeFactory = new H2DataTypeFactory();
                break;
        	default:
        		dataTypeFactory = new DefaultDataTypeFactory();
		}
		
		return dataTypeFactory;
	}
	
	/*
	protected static IDatabaseConnection iDatabaseConnection;
	
	@PostConstruct
	public void initDBUnitBase() throws SQLException {
		// create DataBaseDataSourceConnectionFactoryBean using above DatabaseConfigBean and DataSource
		// DatabaseConfigBean uses pojo set/get instead of dbunit.DatabaseConfig.setProperty()  1:1
		iDatabaseConnection = new DatabaseDataSourceConnection (dataSource);

	}
	*/
	
	// Set database configuration for table types TABLE, VIEW
	// get datasource [for tenant id from execution context (see BaseTest ??)], inject one from context
	
	
	// @DbUnitConfiguration(databaseOperationLookup=MicrosoftSqlDatabaseOperationLookup.class)
	
	// @DbUnitConfiguration(databaseConnection={"dataSource", "customerDataSource"})
	// possibly create static 
	
}
