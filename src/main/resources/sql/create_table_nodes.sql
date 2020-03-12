CREATE TABLE nodes
( name varchar(255) PRIMARY KEY NOT NULL UNIQUE,
  host varchar(255),
  creds varchar(255),
  port integer,
  status varchar(32),
  node_type varchar(64),
  lock boolean
);
