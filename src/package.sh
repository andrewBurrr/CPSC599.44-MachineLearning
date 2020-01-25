#!/usr/bin/bash

# TODO validate script

# begin script

# named parameters
class_directory = $1
app_name = $2
executing_class = $3


# compile java code and create jar
function main () {
    javac -classpath $class_directory $executing class
    jar cvfe $app_name $executing_class $class_directory/*.class
}

# call function main
main

# end script