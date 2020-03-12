CREATE TABLE process_audit
( id varchar(255) PRIMARY KEY NOT NULL UNIQUE,
  owner varchar(255),
  data varchar(2048),
  lock boolean
);
