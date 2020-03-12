About
==================

This is a simple Spring Boot project designed as a web service with 2 end points and asynchronous process execution functionality 

DB configuration
==================

In the case of PostgresSQL as the main database of service create a database rpadata first:

```
psql -U postgres
create database rpadata;
```

List all databases to see a newly created database on the list:

```
\l
```

To connect to a database use the command:

```
\c rpadata
```

To see the table's scheme use the command:

```
\d <table name>

select * from nodes;

select * from processes;

select * from process_audit;


```



REST web interface
==================

All resources are available from two endpoints:

```
/processes

/audit/{table_name}
```

Example of requests:

```
POST https://localhost:8443/processes

{
	"bpId": "d3e7258a-2b70-4e92-bd19-7f3de7612878",
	"name": "test 5",
	"runScriptName": "java -version",
	"processStatus": "CREATED",
	"lock": false
}

GET https://localhost:8443/audit/process_audit

```



Starting
==================

Start webservice:

```
java -jar <path to jar file>
```

The service will start on localhost:8443


Node configuration
==========================

Credentials to nodes are saved in a separate service - KeyService.

For demonstration purpose creds are taken from resources/application.properties file, section ## node creds



 




