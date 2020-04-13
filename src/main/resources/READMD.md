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
###2. Pass in command line arguments
```$xslt
-c -r sample.yml driverdatabase.csv output.txt Example-input.txt
```