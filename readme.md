# Simple Employee Clocking in Management
## Context
At our university there is a special assessment that we shall pass before we can start an internship. The assessment last around two (2) to three (3) days during which students are given a project to work on. Students are free to choose whatever technology stack suits best to them, the only constraint is that it shall be a web technology stack. 
The assessment's goal is to check students'knowledge and skills if they are able to start tackling real world projects. Besides, it helps students to gain confidence with their capabilities. Indeed students tackle every part of the project, from the user interface development to the database design. 
Students are also required to provide a to-do list so that:
- they have an estimate for the project 
- the actual hours they worked on it and
- they can keep track of their progress

Hope you can find something for you here.
Enjoy.

## About the project
This project is a simple employee clocking in management. Employers can set the working time for a particular week of an employee in order to know his/her actual pay.

## Technology
- Java
    - A general-purpose programming language. A popular object oriented language amoung big companies. Object oriented is a very important concept for our source code to be more maintainable and readable.
- Spring Boot Framework
    - This is a powerful framework developed with Java that let us build industrial-grade software. It allows software developers to delegate many repetitive and daunting tasks to the framework and thus focus more on the most critical and important parts of a project. 
- Thymeleaf
    - A popular template engine that works seamlessly with Spring Framework. It offers a wide range of useful tags.
- PostgreSQL
    - A robust relational database management. There is no specific reason to why choosing this for this particular project but to get used to working with it.
- JavaScript:
    - It is a programming language that we can rarely avoid as a web developer.

## How to run
1. Clone this repository
2. Change PostgreSQL login in ConnGen class
3. Executes the script in a PostgreSQL Database
4. Open a command line and executes the following command inside this repository: ** mvnw spring-boot:run **

## User Interface
> "Quick start a project by designing its UI first"

Here are the most important user interfaces of this project.

- Employee list page: it lists all employees in the company.
  - The buttons'labels are self-explanatory: "Pointage" means clocking in, "Fiche de paie" means Pay slip.

![Employee list](/docs/ui_images/front_office.PNG)

- Clocking in: It is the main page of this project. It is the manager responsibility to inputs the working hours of each individual employee for a particular week of the year.
  - Buttons
    - "Valider et afficher fiche" means Validate and Show Pay slip

![Clocking in](/docs/ui_images/clocking_in.PNG)

- Pay slip: For this project we assumed a weekly salary

![Pay slip](/docs/ui_images/pay_slip.PNG)

- Statistics: We can see the real weekly pay of each employee and a table showing the total hours and amounts of each hour type.

![Statistics](/docs/ui_images/statistics.PNG)

- Crud: These pages allow us to insert, update, delete and read the data in our the database
  - Crud category employee
![Crud category employee](/docs/ui_images/crud_category_employee.PNG)
  - Crud employee
![Crud employee](/docs/ui_images/crud_employee.PNG)
  - Crud overtime
![Crud overtime](/docs/ui_images/crud_overtime.PNG)
  - Crud user
![Crud user](/docs/ui_images/crud_user.PNG)
  - Crud premium hour
![Crud premimum](/docs/ui_images/crud_premium.PNG)
  - Crud max premium hour
![Crud max premium hour](/docs/ui_images/curd_max_premium_hour.PNG)

## Database Schema
> "Simplify your life as a developer by spending more time designing your database"

As much as we can, we should put effort to design the database to avoid unnecessary lengthy code at the application layer of our software.
![Conceptual Data Model](/docs/ui_images/cdm.png)

## Other documents
- To-do list: /docs/gestion-ressources-humaines.ods (in French)

## Keywords