CREATE TABLE permission(id INTEGER,url varchar(255),name varchar(255),description varchar(255),pid INTEGER);
CREATE TABLE role(id INTEGER PRIMARY KEY,name varchar(255));
CREATE TABLE role_permission(role_id INTEGER,permission_id INTEGER);
CREATE TABLE user(id INTEGER PRIMARY KEY,username varchar(255),password varchar(255));
CREATE TABLE user_role(user_id INTEGER,role_id INTEGER);