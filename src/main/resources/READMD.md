#CGARMA READ-ME 
## How to initialize program

###1. Set parameters for experiments 
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

###2. Create input rule file according to below scheme:
Example-input.txt
```$xslt
Name: 
Example-input
Rules:
[antecedent_11, ... antecedent_1k],[consequent_11,...,consequent_1k]
[antecedent_21, ... antecdent_2k],[consequent_21,...,consequent_2k]
```

###3. Usage
-c     --config           Path to the Configuration for the system
-i     --input            Path to the input file
-k     --input            Path to the input file
-o     --output           Path to the Output file
-w     --output           Path to the Output file
-v     --verbose          More detailed terminal messages
-pre   --weights          Weight assigned
-pro   --processor        Operation is Processing only.");
-post  --postprocessor    Operation is Postprocessing only.");
-all                      Operation Perform all Pre-Processing, processing and Post Processing
-help                     Show this help


###4. Sample command line arguments
```$xslt
1) -c -r sample.yml driverdatabase.csv output.txt Example-input.txt
2) java -jar {appname} "-p", "5","-k", "src/test/resources/knownRules.txt","-pro",
    "-c","src/test/resources/config.yml","-i","src/test/resources/driversTest.csv",
    "-o", "src/test/resources/Output.txt"
```