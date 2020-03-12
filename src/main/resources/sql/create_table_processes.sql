CREATE TABLE processes
( bpid varchar(36) PRIMARY KEY NOT NULL UNIQUE,
  name varchar(255),
  runid varchar(36),
  started date,
  finished date,
  process_status varchar(32),
  source_url varchar(255),
  run_script varchar(255),
  stop_script varchar(255),
  node_name varchar(255),
  lock boolean
);
