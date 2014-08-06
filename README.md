generic-reporter
================

An amazingly easy web application to generate JSON responses, PDF and HTML reports of data in your mysql database. Just Create a procedure with the data you want to show and you will have an API returning JSON response, or a report in PDF or HTML format.

**DEMO**
You can access a live deployment of this app here : *http://162.248.10.127:8080/generic-reporter/*

In the sample webapp, the database has three tables:

Users: id (INT), username(VARCHAR), password(VARCHAR), email(VARCHAR)
Plans: id(INT), name(VARCHAR), description(VARCHAR), price(FLOAT)
User_Plan : user_id(int), plan_id (int)


It also has four procedures that get different data from the three tables, the procedures are:
get_all_plans
get_all_users
get_all_users_and_their_plans
get_user_plan
get_uset_plan_by_username

The application handles procedures with or without parameters. Procedure Parameters are sent as part of the HTTP request.


To generate json data, use the extension .json
To generate html reports, use the extension .html
To generate PDF reports, use the extension .pdf

Simple and easy :)

Enjoy!

