# C195

## Title
Planning App

## Purpose of application
A multi-lingual GUI for managing appointments with customers.

## Author and contact
Jonathan Dowdell <jdowde2@wgu.edu>

## IDE Version
IntelliJ IDEA 2021.1.1 (Community Edition)
Build #IC-211.7142.45, built on April 30, 2021
Runtime version: 11.0.10+9-b1341.41 amd64
VM: Dynamic Code Evolution 64-Bit Server VM by JetBrains s.r.o.
Windows 10 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 768M
Cores: 4

Kotlin: 211-1.4.32-release-IJ7142.27

## JDK version


## JavaFX version
openjfx-17.0.1

## MYSQL Connector Version
mysql-connector-java-8.0.25

## Database Schema 

# How to run app
Create a `database.xml` file in the root of the directory of the repo with the following contents (being sure to fill in the nodes):

```xml
<?xml version = "1.0"?>
<database>
    <server></server>
    <port></port>
    <name></name>
    <user></user>
    <password></password>
</database>
```

You will also need a user in the database with the username "test".

## Additional report

For the additional report, I chose to get a rundown of all customers per division.