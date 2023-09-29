Create database Assignment1;
use Assignment1;

CREATE TABLE Program (
  Program_Code CHAR(10) PRIMARY KEY,
  Program_Name VARCHAR(255) NOT NULL,
  Program_Description TEXT,
  Degree_Type VARCHAR(50),
  Program_Duration INT
);

CREATE TABLE Course (
  Course_Code CHAR(10) PRIMARY KEY,
  Course_Name VARCHAR(255) NOT NULL,
  Course_Description TEXT,
  Credit_Hours INT,
  Prerequisites VARCHAR(255)
);

CREATE TABLE Course_Prerequisite (
  Prerequisite_Course_Code CHAR(10),
  Prerequisite_Course_Name VARCHAR(255),
  PRIMARY KEY (Prerequisite_Course_Code, Prerequisite_Course_Name),
  FOREIGN KEY (Prerequisite_Course_Code) REFERENCES Course(Course_Code)
);


CREATE TABLE Faculty (
  Faculty_ID CHAR(10) PRIMARY KEY,
  Faculty_Name VARCHAR(255) NOT NULL,
  Email VARCHAR(255),
  Department VARCHAR(255)
);

CREATE TABLE Student (
  Student_ID CHAR(10) PRIMARY KEY,
  Student_Name VARCHAR(255) NOT NULL,
  Email VARCHAR(255),
  Major VARCHAR(255)
);

CREATE TABLE Student_Major (
  Student_ID CHAR(10),
  Major VARCHAR(255),
  PRIMARY KEY (Student_ID, Major),
  FOREIGN KEY (Student_ID) REFERENCES Student(Student_ID)
);


CREATE TABLE Department (
  Department_Code CHAR(10) PRIMARY KEY,
  Department_Name VARCHAR(255) NOT NULL,
  Department_Description TEXT
);

CREATE TABLE Campus (
  Campus_Code CHAR(10) PRIMARY KEY,
  Campus_Name VARCHAR(255) NOT NULL,
  Location VARCHAR(255),
  Contact_Information VARCHAR(255)
);


CREATE TABLE Resource (
  Resource_ID CHAR(10) PRIMARY KEY,
  Resource_Name VARCHAR(255) NOT NULL,
  Resource_Description TEXT,
  Availability VARCHAR(255),
  Location VARCHAR(255)
);


CREATE TABLE News (
  News_Title VARCHAR(255) PRIMARY KEY,
  News_Description TEXT,
  Author VARCHAR(255),
  Publication_Date DATE
);

CREATE TABLE Enrollment (
  Enrollment_ID CHAR(10),
  Enrollment_Date DATE,
  Grade VARCHAR(2),
  PRIMARY KEY (Enrollment_ID),
  FOREIGN KEY (Enrollment_ID) REFERENCES Course(Course_Code)
);
















































