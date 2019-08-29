CREATE TABLE IF NOT EXISTS Person (ID int PRIMARY KEY auto_increment,Name varchar(255),Age int,Address varchar(255));
CREATE TABLE IF NOT EXISTS MultiPKTable (Date VARCHAR (10) not null,userId INT not null,interest double,principal double,rate double);
ALTER TABLE MultiPKTable ADD PRIMARY KEY (Date,userId);
