## Instalation:
#### 1. Install Eclipse 
  Version: 2021-06 (4.20.0) 
#### 2. Install Xtext
  Version: Xtext 2.26.0   
#### 3. Install Matlab
  Version: MATLAB 9.8 R2020a    
  If you have never used S-TaLiRO, download and install from this https://sites.google.com/a/asu.edu/s-taliro/s-taliro/download
#### 4. Install Home-Assistant
  Version: Python 3.8
## How to use ?
Note: You should make sure that all the softwares have been installed and can be used normally.
#### 1. Development Support
For the development support module, we provide two editors for specifying MetTap rules and used-defined properties as the interface for users.
We implement the development environment for MetTap rules with Xtext, an Eclipse-based tool for designing textual DSLs. 
With this tool, a domain-specific editor has some functions, such as syntax highlighting and syntax errors display. 

Step1: Create an Xtext project in Eclipse     
Step2: Modify .xtext file    
Step3: Run this project to write MetTap rules   
Step4: Create a Java project named MetTap in Eclipse     
Step5: Create AddNewProperty.class file    
Step6: Import codes and run this project to define properties

#### 2. Runtime Support
For the runtime support module, we use Home Assistant to manage smart devices. 
A rule transformation component is given to translate MetTap rules into Home Assistant rules.
We integrate our runtime support module with Home Assistant (HA).
We use Home Assistant (HA) as the part of our running support platform. 
The runtime support module of our platform contains three components: device configuration which supports deployment of sensors/actuators, rule transformation which translates MetTap rules to HA rules, and execution which performs the trigger activities of specific devices.  

Step1: Create an python project in Python IDE           
Step2: Create HA.py file and import codes      
Step3: Writing the mapping information of entity IDs and events/states, such as:entities.json, vocabularies.json      
Step4: Load MetTap rules  
Step4: Establish a connection with Home Assistant system     
Step5: Run this project   

#### 3. Runtime Verification
For the runtime verification module, we provide the monitoring function to verify the properties and give feedback with verification results.
In this module, we generate monitors with MTL properties. 
These properties include the properties translated from MetTap rules automatically and user-defined ones.  
  
Step1: Create Get.class file in MetTap project   
Step2: Create Put.class file in MetTap project   
Step3: Create MTL.class file in MetTap project   
Step4: Create MapMTL.class file in MetTap project   
Step5: Create Trace_Slice.class file in MetTap project   
Step6: Create Runtime_Verification.class file in MetTap project   
Step7: Import these codes    
Step8: Create RV.m file in Matlab  
Step9: Import the codes     
Step10: Package the RV.m file into .jar file  
Step11: Import this jar file in MetTap project
Step12: Run this project

#### 4. Case Study
In this section, we show how MetTap can be used to specify rules with timing constraints and verify whether applications execute correctly in home-based IoT system.  

If you want to reproduction experiment, you can load these data to test it in related procedures  
