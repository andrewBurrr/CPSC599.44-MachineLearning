#!/usr/bin/bash

# TODO validate script

# begin script

# compile java code and create jar
function main () {
    javac `-classpath $(${1}) $(${2})`;
    jar `cvfe $(${3}) $(${2}) $(${1})/*.class`;
}

# call function main
main $1 $3 $2;

# end script