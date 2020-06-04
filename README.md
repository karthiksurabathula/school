# School Application
This application is developed with Angular 8 and Spring boot (Java 8) it could accommodate multiple schools at once.

I have covered most common functions that are required by a school to manage their own online portal.

Application is designed to be Resilient with Kubernetes Autos-scalling and recovery features.

If you want to try the application i have also created docker-compose file which is simpler to start with.

## Requirements
**DB Requirements**  
Version: Myqsl 8  
Database Name: school  
DB User name: springboot  
DB assword: Password  
  
**Angular Requirement**  
Angualr 8  
  
**Spring Boot Requirement** 
Java 8  

## Setup
Docker container with pre-configured application are available in public repository, you can use those images or rebuild based on your requirement.
* [Build application](https://github.com/karthiksurabathula/school/wiki/Build-application)
* [Docker-compose](https://github.com/karthiksurabathula/school/wiki/docker-compose-setup-instructions) (Not recommended for high availability environment)
* [Kubernetes](https://github.com/karthiksurabathula/school/tree/master/kubernetes) (High availability cluster)

Instrunction to access application are specified in Setup pages
