#! /bin/bash

rm -r /usr/local/Cellar/tomcat/8.5.11/libexec/webapps/api*
ant clean
ant -Dwar.name=api deploy
