# school-app
School application developed with Spring boot backed, Angular 8 UI and Mysql 8 DB

The application address multiple schools management in one application, below is the functionality that was implemented.

Authentication and Autorization(Role based access) using spring security.

**User types:**  
Super user - su  
Admin User - this user has access to a particular school and can make any changes to the school  
Teacher  
Student  

**Functionality:** All the funtionality of each school is isolated from other  
--> Create City  
--> School  
--> Class  
--> Section  
--> Subject   
--> Subject Class Mapping  
--> Staff  
--> Class Subject to Staff Mapping: Mapping for Subject to Staff  
--> Student  
--> Period  
--> Timetable: Daily schedule per section  
--> Exam  
--> Exam timetable: Exam schedule  
--> Marks  
--> Attendence traker with ability to add notes  
--> Marks and timetable completion tracking  
--> Syllabus updates
--> Announcements  
--> User Management  
--> Dummy data creation option (only available for Super user)
  
**DB Requirements**  
Version: Myqsl 8  
Database Name: school  
DB User name: springboot  
DB assword: Password  
  
**Angular Requirement**  
Angualr 8  
  
**Spring Boot Requirement** 
Java 8  
  
When ever user is created, or there is password reset link is generated and stored in "outboud" table with relavent details, if you want to send to the user additional coding would be required

All the functionality is divided in accordance to my knowledge of school working. 

In case of any application and UI functionality issues\errors please feel free log a issue. I will try to resolve it at earlier possible. I would not be looking into UI css issues.

Please feel free to download and use the appliaction at your will.

