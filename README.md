# spring-boot-angular5
1. Assumptions maven and MS SQL Express are installed on your machine and database is defined to match application.properties spring-data configuration.

Build and run server from windows command line
	A. build server with "mvn clean install" from user-portal directory
	B. run server with "mvn exec:java" from user-portal directory

Build and run client from windows command line
	A. build & run client with "ng serve --proxy-config proxy.config.json" from portal-app directory

The sample maven configuration to build spring boot and angular 5 project together as a war file.
The actual implementation was posted on [spring boot angular Deployment](http://www.devglan.com/spring-boot/spring-boot-angular-deployment)
This is a sample project using Angular 5 and spring Boot.Spring boot has been used to expose REST Endpoints and the client is written in angular. 
This is a complete web application with a connectivity to mysql database. We have used spring data to connect to the database and perform CRUD
operation.

The application has CRUD operation example for user entity. Here, you can create
a user, read users record and also delete user. The complete explanation can be 
found on my blog - [spring boot angular 5 example](http://www.devglan.com/spring-boot/spring-boot-angular-spring-data-example)

This project uses following versions:

1. Spring Boot v1.5.9
2. Angular v5.0.0
3. Maven
