CREATE TABLE node_audit 
( id varchar(1024) PRIMARY KEY NOT NULL UNIQUE,
  owner varchar(255),
  data varchar(4096),
  lock boolean
);
