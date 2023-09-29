CREATE DATABASE db_crm;
USE db_crm;

CREATE TABLE Status(
    id INT AUTO_INCREMENT,
    name VARCHAR(50),
    description VARCHAR(50),
    
    PRIMARY KEY(id)
);

CREATE TABLE Role(
    id INT AUTO_INCREMENT,
    name VARCHAR(50),
    description VARCHAR(50),

    PRIMARY KEY(id)
);

CREATE TABLE User(
	id INT AUTO_INCREMENT,
	email VARCHAR(50),
	password VARCHAR(50),
	fullName VARCHAR(50),
	address VARCHAR(50),
	phoneNumber VARCHAR(10),
	id_Role INT,
	
	PRIMARY KEY(id)
);

ALTER TABLE User ADD CONSTRAINT FK_id_Role_User
FOREIGN KEY(id_Role) REFERENCES Role(id) ON DELETE SET NULL;

CREATE TABLE Project(
	id INT AUTO_INCREMENT,
	name VARCHAR(50),
	description VARCHAR(50),
	startDate TIMESTAMP,
	endDate TIMESTAMP,
	id_User INT,
	id_Status INT,
	
	PRIMARY KEY(id)
);

ALTER TABLE Project ADD CONSTRAINT FK_id_User_Project 
FOREIGN KEY(id_User) REFERENCES User(id) ON DELETE CASCADE;
ALTER TABLE Project ADD CONSTRAINT FK_id_Status_Project 
FOREIGN KEY(id_Status) REFERENCES Status(id) ON DELETE SET NULL;

CREATE TABLE Task(
	id INT AUTO_INCREMENT,
	name VARCHAR(50),
	description VARCHAR(50),
	startDate TIMESTAMP,
	endDate TIMESTAMP,
	id_Project INT,
	id_Status INT,
	
	PRIMARY KEY(id)
);

ALTER TABLE Task ADD CONSTRAINT FK_id_Project_Task 
FOREIGN KEY(id_Project) REFERENCES Project(id) ON DELETE CASCADE;
ALTER TABLE Task ADD CONSTRAINT FK_id_Status_Task 
FOREIGN KEY(id_Status) REFERENCES Status(id) ON DELETE SET NULL;

CREATE TABLE Project_User(
	id_Project INT,
	id_User INT,
	
    PRIMARY KEY(id_Project,id_User)
);

ALTER TABLE Project_User ADD CONSTRAINT FK_id_Project_Project_User 
FOREIGN KEY(id_Project) REFERENCES Project(id) ON DELETE CASCADE;
ALTER TABLE Project_User ADD CONSTRAINT FK_id_User_Project_User 
FOREIGN KEY(id_User) REFERENCES User(id) ON DELETE CASCADE;

CREATE TABLE Task_User(
	id_Task INT,
	id_User INT,
	
	PRIMARY KEY(id_Task,id_User)
);

ALTER TABLE Task_User ADD CONSTRAINT FK_id_Task_Task_User 
FOREIGN KEY(id_Task) REFERENCES Task(id) ON DELETE CASCADE;
ALTER TABLE Task_User ADD CONSTRAINT FK_id_User_Task_User 
FOREIGN KEY(id_User) REFERENCES User(id) ON DELETE CASCADE;

INSERT INTO Role(name, description) VALUES
('ADMIN', 'Admin role'),
('LEADER', 'Leader role'),
('MEMBER', 'Member role');

INSERT INTO Status(name, description) VALUES
('Planning', 'Planning status'),
('Doing', 'Doing status'),
('Done', 'Done status');
