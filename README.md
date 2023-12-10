# CS 1555 Project Phase 3 - JDBC Application
Designed by Ethan Wells, Hala Nubani, and Ben Kiddie.

## Overview
This JCBC application creates a textual interface that allows users to interact with the ArborDB database.
It contains 21 total functions: 2 for managing the database connection, 13 for creating, updating, and 
modifying data in various tables, and 6 for performing various analytical operations. 

## Start-Up Guide
Before starting the application, make sure that the code is compiled and ran (respectively, for Windows) as such:
- javac -cp "postgresql-42.6.0.jar;." ArborDB.java
- java -cp "postgresql-42.6.0.jar;." ArborDB 

Before attempting to connect, ensure that a DB session is active and all .sql files in the repository
have been executed. 

The `connect` command must be run before any other function, as all other functions require an active database
connection to properly function. Attempting to run any function without a connection established will result in
a warning to the user.

## Functional Overview
Below is an overview of each of the 21 functions. They can be activated by entering the corresponding number
within the main menu.
1. `connect`\
   This command will require the user to enter their PostgreSQL username and password before connecting 
   to PostgreSQL via JDBC. Please ensure you are connecting to the same session that hosts the `arbor_db` schema. 
   If a connection is already active, this function will inform the user and return to the main menu.
2. `addForest`\
   This command will prompt the user to enter a name, area, acid level, MBR XMin, MBR XMax, MBR YMin,
   and MBR YMax. It will then generate a new tuple in the `FOREST` relation using this data. 
3. `addTreeSpecies`\
   This command will prompt the user to enter a genus, epithet, ideal temperature, largest height, and 
   raunki-aer life form specification. It will then generate a new tuple in the `TREE_SPECIES` relation
   using this data. 
4. `addSpeciesToForest`\
   This command will prompt the user to enter a forest_no, genus, and epithet. It will then generate a new tuple
   in the `FOUND_IN` relation using this data.
5. `newWorker`\
   This command will prompt the user to enter a SSN, first name, last name, middle initial, rank, and a state
   abbreviation. It will then generate a new tuples in the `WORKER` and `EMPLOYED` relations using this data.
6. `employWorkerToState`\
   This command will rpompt the user to enter a worker SSN and state abbreviation. It will then generate a new
   tuple in the `EMPLOYED` relation using this data.
7. `placeSensor`\
   This command will prompt the user to enter an energy level, location of deployment (X and Y coords), 
   and a maintainer id. It will then generate a new tuple in the `SENSOR` relation using this data. Note that this
   this operation will fail if the sensor's location is not within one of the states in which its maintainer is
   employed.
8. `generateReport`\
   This command will display all sensors currently deployed in table form. It will then ask the user to enter the
   ID of the sensor to generate a report, or to enter -1 to exit. If the user enters a sensor ID, they will be prompted
   for a report timestamp and temperature. It will then generate a new tuple in the `REPORT` relation using this data.
9. `removeSpeciesFromForest`\
   This command will prompt the user to enter a forest_no, genus, and epithet. It will then delete the `FOUND_IN`
   tuple corresponding with this data.
10. `deleteWorker`\
   This command will prompt the user for a worker SSN. It will then delete the `WORKER`, `EMPLOYED`, and `SENSOR`
   tuples corresponding with this SSN.
11. `moveSensor`\
   This command will prompt the user for a sensor ID and new location (X and Y coords). If the user enters -1 for 
   the sensor ID, they will be returned to the main menu. Otherwise, it will update the associated tuple in `SENSOR`
   to use the newly provided location. Note that this command will fail if the sensor's new location is not
   within one of the states in which its maintainer is employed. Additionally, this command will fail if there
   are no sensors in the database.
12. `removeWorkerFromState`\
   This command will prompt the user for a worker SSN and state abbreviation. It will then remove the tuple in 
   `EMPLOYED` associated with this worker and state. Additionally, all sensors maintained with this worker will be
   reassigned to the worker in the state with the lowest SSN, or deleted if this is not possible. 
13. `removeSensor`\
   This command will first ask the user if they would like to remove 'all' sensors, or 'select' sensors for removal.
   If the user selects all, they will be asked for additional confirmation. If given, all sensors in the database
   will be removed. If users opt to select sensors, they will be shown sensors in the database 1 by 1, with the option
   to skip this sensor by entering 0, remove it by entering its ID, or return to the menu by entering -1. If anything 
   other than these three values is entered, the user is warned before being shown the same sensor again.
14. `listSensors`\
   This command will prompt the user for a forest_no. It will then display all of the sensors located within the 
   given forest in a table format. If no such sensors exist, the user is told "No sensors found in the specified 
   forest."
15. `listMaintainedSensors`\
   This command will prompt the user for a worker SSN. It will then display all of the sensors maintained by the
   given worker in a table format. If no such sensors exist, the user is told "No sensors maintained by given
   worker."
16. `locateTreeSpecies`\
   This command will prompt the user for a regex patterns for a genus and epithet. It will then display all of the
   forests in which a tree species matching these patterns is found in a table format. If none exist, the user is
   told "No forests found with the specified tree species pattern."
17. `rankForestSensors`\
   This command will display all forests in ranked order by how many sensors are in them. If no forests are in the
   database, the user is told "No forests to rank."
18. `habitableEnvironment`\
   This command will prompt the user for a genus, epithet and value k (years from present to consider data). It will
   then display all forests with average recorded temperatures in the past k years that are habitable for that species.
   if no such forests are found, the user is told "No habitable environments were found for the given species in the
   given time frame."
19. `topSensors`\
   This command will prompt users for values k (number of sensors) and x (months back from present to consider reports).
   It will then display the top k sensors based on the number of reports generated in the past x months. If no 
   sensors are in the database, the user is told "No sensors found."
20. `threeDegrees`\
   This command will prompt the user for two forest numbers. It will then determine the shortest path between
   these forests with a maximum of three hops, where a hop is given by any two forests sharing some tree species.
   If no such path is found, the user is told "No three-hop path exists between these forests."
21. `exit`\
   This command will close the database connection and exit the program.