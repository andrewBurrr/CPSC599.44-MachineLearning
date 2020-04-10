#!/bin/bash

# Beginning of script

function Get-Maven {
	echo "Setting up Maven";
	curl -C - --output apache-maven-3.6.3-bin.tar.gz https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz;
	tar xvzf apache-maven-3.6.3-bin.tar.gz;
	export M2_HOME=$PWD/apache-maven-3.6.3;
	export M2=$M2_HOME/bin;
	export PATH=$M2:$PATH;
}

function Get-Project {
	git clone https://github.com/andrewBurrr/CPSC599.44-MachineLearning.git;
}

function Get-Jar {
	# cd CPSC599.44-MachineLearning;
	mvn clean install;
}

function Run-Program {
	java -jar target/rules_599-1.0-SNAPSHOT-spring-boot.jar;
}

function Main {
	Get-Maven;
	# Get-Project; # comment this out if the project is already included
	Get-Jar; # this should be a separate step for submission (likely just an instruction in a readme.md)
	Run-Program; # also in a readme.md
}

Main;

# End of Script