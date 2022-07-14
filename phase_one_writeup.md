# Project Phase 1 Group 7 Write Up

### Optional Tasks Attempted:
* Task 1.7 Organization App input error handling
* Task 1.8 Date Formatting


### Changes to Provided Code
* Task 1.3 Organization App display total donations for fund: Changes were made to the displayFund() method in the UserInterface.java class. Particularly, created an integer variable “total” to track total donations as shown on line 132 in the figure below and then created a double variable, “percent” to capture the percent of the fund target met through donations as shown on line 146 in the figure below.  Finally, on line 148, the total donations amount and the percent of the target met were printed to the UI using System.out.println as shown in the figure below.

     <img src=images/image1.png height="400">


* Task 1.7 Organization App input error handling: Changes were made to the start() and createFund() methods in the UserInterface.java class. The changed methods are shown in the figure below. The changes made for error handling are discussed below:
  * In the start() method, if the user did not provide a fund number between 0 and the total fund size, an error message was displayed and the user was prompted to provide another fund number to access information about the funds available or create a new fund. These changes are displayed in the code below.

      <img src=images/image3.png height="400">

  * In the createFund() method if the user provided a fund name that was blank or already an existing fund name, an error message was printed and the user was asked to provide another fund name. Furthermore, if the description or the target amount for the fund was blank or not a positive integer respectively then an error message was displayed and the user was prompted to provide a fund description or fund amount.

     <img src=images/image2.png height="400">


* Task 1.8 Date Formatting: Changes were made to the displayFund() method in the UserInterface.java class and the changes made for date formatting are shown in the figure below between lines 134-141. First, the donation date provided in string format was substring to characters from 0 to 10 on line 134. Then the string date was parsed into Java Date format on line 137 based on the dateFormat of the string date. Then the dateFormat required was set on line 138. Finally the Java Date was formatted to the required dateFormat on line 139 in string format and the date was printed in the required format on line 140.

     <img src=images/image1.png height="400">


### Bugs Found in 1.2
* Spelling error in attemptLogin() of DataManager class. “descrption” changed to "description".


### Other Known Bugs/Issues
* None

### Instructions if changed
* We have provided a “/data” folder in the repository. When starting MongoDB, run the command ./bin/mongod –dbpath [path_to_repo_location/data].
* When running the Organization app from a Java IDE, pass in the arguments "prkapoor W@terfall" to run the UserInterface.java to ensure the organization app can access the data on the data folder.

### Team member contributions
* Pranay - Completed Tasks 1.3 and Optional Tasks 1.7 and 1.8
* Harsh - Completed Tasks 1.1 and 1.2
* Lenny - Completed Tasks 1.1 and 1.2
Each member of the team also tested and checked the other members' work.
