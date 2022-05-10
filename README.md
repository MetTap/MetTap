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
Note: You should make sure that all the above softwares have been installed and can be used normally.
#### 1. Development Support
For the development support module, we provide two editors for specifying MetTap rules and used-defined properties as the interface for users.
We implement the development environment for MetTap rules with Xtext, an Eclipse-based tool for designing textual DSLs. 
With this tool, a domain-specific editor has some functions, such as syntax highlighting and syntax errors display. 

Step1: Create an Xtext project in Eclipse     

Step2: Modify .xtext file with the MetTap.xtext codes      

Step3: Run this project and the specific process can refer to https://www.eclipse.org/Xtext/documentation    

Step4: Write MetTap rules in the generated entity editor and they will be stored in MetTapRules.txt   

Step5: Create a Java project named MetTap in Eclipse     

Step6: Import AddNewProperty.java file      

Step7: Run this file  

Step8: Define properties in the interface, which will be stored in User-defined Properties.txt file  

#### 2. Runtime Support
For the runtime support module, we use Home Assistant to manage smart devices. 
A rule transformation component is given to translate MetTap rules into Home Assistant rules.
We integrate our runtime support module with Home Assistant (HA).
We use Home Assistant (HA) as the part of our running support platform. 
The runtime support module of our platform contains three components: device configuration which supports deployment of sensors/actuators, rule transformation which translates MetTap rules to HA rules, and execution which performs the trigger activities of specific devices.  

Step1: Create an python project in Python IDE           

Step2: Import HA.py codes      

Step3: Writing the mapping information of entity IDs and events/states, such as:entities.json, vocabularies.json   

Step4: Load MetTap rules from Rules.txt

Step5: Run Home Assistant system and this project   

Step6: Change the state of devices/sensors in Home Assistant system

Step7: If the triggers are satisfied, the action will be executed. Otherwise,it will not be executed    

Step8: Store the state data in sensor_data.csv    

#### 3. Runtime Verification
For the runtime verification module, we provide the monitoring function to verify the properties and give feedback with verification results.
In this module, we generate monitors with MTL properties. 
These properties include the properties translated from MetTap rules automatically and user-defined ones.  


Step1: Import RV.m file into Matlab  

Step2: Package the RV.m file into .jar file

  _Step2.1: Enter the "deploytool" command under the command window in Matlab to open the deployment tool     

  _Step2.2: Create a new project Runtime.prj     

  _Step2.3: Select "java package" and click "ok"     

  _Step2.4: Click "add" to add a Java class named Class1    

  _Step2.5: Click "add files" to add the RV.m file   

  _Step2.6: Click "compile" to generate the Runtime.jar   

Step3: Import .java files of Runtime Verification package into MetTap project   

Step4: Reference the following two jar packages in MetTap project: Matlab installation directory\toolbox\javabuilder\jar\javabuilder.jar and Runtime.jar  

Step5: Run the Runtime_Verification.java and the rules will be verified    

#### 4. Demos 
In order to demonstrate the effectiveness of MetTap, we apply it to the real smart home scenarios and verify whether running states satisfy given properties. Then, we have carried out the experiments on an open source benchmark to evaluate the efficiency of the runtime verification approach.  

If you want to reproduction experiment, you can load these data to test it in related procedures  

Case Study Rules---Rules.txt  

Case Study Data---sensor_data.csv  

With the requirements of users in home-based IoT, we construct several rules in this scenario. The result of the experiments show that when the triggers are satisfied, the rules can be triggered correctly. In order to evaluate the runtime verification approach, it is necessary to make the equipment and environment produce enough running states to cover various possible use cases. We change the state of devices manually to simulate the situation that the rules cannot be executed correctly in real world. We select several rules for verification, such as the rule``IF Event Tom wakes up AND State the temperature below 15 degree Celsius THEN State the AC is on G:[0,25]''. When the triggers are satisfied, the air conditioner is on. We then turn off the air conditioner manually within 25 minutes to simulate that the action cannot be executed correctly. Through experiments, our runtime verification approach effectively found that the rule was not satisfied and generated an error log.

Performance Analysis Rules--- PARules.txt

Performance Analysis Data--- Example.csv

In the first experiment, we consider two factors which will have an influence on the performance of runtime verification, i.e., the number of properties and amount of running state data. We adjust the amount of running state data from 500 to 5000. At the same time, we also adjust the number of properties from 10 to 50. For each experiment, the simulation system runs 30 times, and the time  overhead with a specific amount of data and number of properties is recorded and averaged.
