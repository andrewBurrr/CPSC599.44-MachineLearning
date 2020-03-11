#!/bin/bash
# begin script
# TODO validate script

# arguments are (1) class directory, (2) executing class, (3) app name
# this currently does not work, lol at one point it did
# compile java code and create jar
function main () {
    javac `-classpath $(${1}) $(${2})`;
    jar `cvfe $(${3}) $(${2}) $(${1})/*.class`;
}

# call function main
main $1 $3 $2;

# end script