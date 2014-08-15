#!/bin/sh

# start the application with a local postgres database
# the local postges database must exit

# to create this database:
# sudo su postgres -c psql
# create user moodly with password 'moodly';
# create database moodly;
# grant all privileges on database moodly to moodly;

sbt \
-Ddb.default.driver=org.postgresql.Driver \
-Ddb.default.url="jdbc:postgresql://127.0.0.1:5432/moodly" \
-Ddb.default.user=moodly \
-Ddb.default.password="moodly" \
-DapplyEvolutions.default=true \
"$@"
