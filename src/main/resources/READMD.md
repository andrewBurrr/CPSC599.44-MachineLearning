**CGARMA READ-ME**

## Setup

### 1. Allow execution permision for setup script: ```chmod +x setup.sh```
### 2. Execute setup script to configure maven (warning this looks scary): ```./setup.sh```
### 3. Enable maven command: ```alias mvn='apache-maven-3.6.3/bin/mvn'```
### 4. Build project to an executable jar: ```mvn clean install```
### 5. Run the program: ```java -jar target/rules_599-1.0-spring-boot.jar --population 5 -pro --config $PWD/src/test/resources/config.yml --input $PWD/src/test/resources/<DATABASE>.csv --output $PWD/<OUTPUTFILE>.txt```

## How to initialize program

### 1. Set parameters for experiments 
Modify the .yml file with the following parameters to match your preferences 
```
spring:
  profiles:
    active: local

logging:
  config: classpath:logback.xml
  #path: classpath:log

trainer:
  metrics:
    support: 0.5
    dataFile: classpath:data.csv

```

### 2. Create input rule file according to below scheme:
Example-input.txt
```$xslt
Name: 
Example-input
Rules:
[antecedent_11, ... antecedent_1k],[consequent_11,...,consequent_1k]
[antecedent_21, ... antecdent_2k],[consequent_21,...,consequent_2k]
```

### 3. Usage
```
-c     --config           Path to the Configuration for the system
-i     --input            Path to the input file
-k     --know            Path to the input knowledge file
-p     --population       Size of breeding population
-o     --output           Path to the Output file
-w     --weight           Weight Assigned
-v     --verbose          More detailed terminal messages
-pre   --preprocessor     Operation is preprocessing only
-pro   --processor        Operation is Processing only
-post  --postprocessor    Operation is Postprocessing only
-all                      Operation Perform all Pre-Processing, processing and Post Processing
-help                     Show this help
```

### 4. Sample command line arguments
```$xslt
1) -c -r sample.yml driverdatabase.csv output.txt Example-input.txt
2) java -jar {appname} "-p", "5","-k", "src/test/resources/knownRules.txt","-pro",
    "-c","src/test/resources/config.yml","-i","src/test/resources/driversTest.csv",
    "-o", "src/test/resources/Output.txt"
```
