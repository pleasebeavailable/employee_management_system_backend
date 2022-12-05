create user employer;
CREATE DATABASE employee_management_db;
GRANT ALL PRIVILEGES ON DATABASE employee_management_db TO employer;
insert into employees (email_id, first_name, last_name) VALUES ('employee1@employee.com', 'test', 'test');
