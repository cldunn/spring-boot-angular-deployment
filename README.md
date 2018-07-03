# spring-boot-angular5
	1. Assumptions maven is installed on your machine

Run the following in order:
	Build client from windows command line
		1. build & run client with "ng build" from portal-app directory

	Build and run server from windows command line
		1. build server with "mvn clean install" from user-portal directory
		2. run server with "mvn exec:java" from user-portal directory

	Run client from windows command line
		1. build & run client with "npm start" from portal-app directory

	Run application from browser
		1. http://4200
	
	Run h2 console (springp.profiles.active=h2)
		1. http://8080/user-portal/h2
		NOTE: enter jdbc:h2:mem:Demo; into JDBC URL on login page
	
		
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
