/*
 * CS1555/2055 Project Phase 3 - Java Application
 * Uses JDBC to interact with ArborDB
 *
 * Authors: Hala Nubani, Ethan Wells, Ben Kiddie
 */

/*
 * Make sure that the code is compiled and ran (respectively, for Windows) as such:
 *  - javac -cp "postgresql-42.6.0.jar;." ArborDB.java
 *  - java -cp "postgresql-42.6.0.jar;." ArborDB
 * 
 * Before attempting to connect, make sure that a DB session is active and all .sql file are executed via DataGrip
 */

import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ArborDB {
    private static Connection connection; // Connection to be maintained with the database
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // For I/O
    
    private static void connect() {
        if (connection == null) { // If a connection is yet to be established, attempt to connect
            String user = "";
            String pwd = "";
            try {
                System.out.print("Input your username: ");
                user = br.readLine();
                System.out.print("Input your password: ");
                pwd = br.readLine();
            } catch (IOException e) {
                System.out.println("I/O error, returning to main menu.");
                return;
            }
            try {
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", user, pwd);
            } catch (SQLException e) { // Thrown when, for any reason, connection cannot be established
                System.out.println("Connection error. Please make sure that the database is active and that your username and password are correct.");
                return;
            }
        } else { // If connection already exists (not null), no need to connect
            System.out.println("Already connected to database.");
            return;
        }
    }

    private static void addForest() {

    }

    private static void addTreeSpecies() {
        
    }

    private static void addSpeciesToForest() {
        
    }

    private static void newWorker() {
        
    }

    private static void employWorkerToState() {
        
    }

    private static void placeSensor() {
        
    }

    private static void generateReport() {
        
    }

    private static void removeSpeciesFromForest() {
        
    }

    private static void deleteWorker() {
        
    }

    private static void moveSensor() {
        
    }

    private static void removeWorkerFromState() {
        
    }

    private static void removeSensor() {
        
    }

    private static void listSensors() {
        
    }

    private static void listMaintainedSensors() {
        
    }

    private static void locateTreeSpecies() {
        
    }

    private static void rankForestSensors() {
        
    }

    private static void habitableEnvironment() {
        
    }

    private static void topSensors() {
        
    }

    private static void threeDegrees() {
        
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC .jar dependency not detected. Please make sure that the library is correctly loaded in and try again.");
            System.exit(1);
        }
        System.out.println("Welcome to ArborDB!\nMade by Ethan Wells, Benjamin Kiddie, and Hala Nubani.");
        while (true) { // Endlessly produce main menu until program is exited
            System.out.print("\nOperations on ArborDB:\n1.  connect\n2.  addForest\n3.  addTreeSpecies\n4.  addSpeciesToForest\n5.  newWorker\n6.  employWorkerToState\n7.  placeSensor\n"
                + "8.  generateReport\n9.  removeSpeciesFromForest\n10. deleteWorker\n11. moveSensor\n12. removeWorkerFromState\n13. removeSensor\n14. listSensors\n"
                + "15. listMaintainedSensors\n16. locateTreeSpecies\n17. rankForestSensors\n18. habitableEnvironment\n19. topSensors\n20. threeDegrees\n21. exit\n"
                + "\nPlease indicate which operation (1-21) you would like to perform: ");
            int op = 0;
            try {
                op = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("The provided input is invalid, please try again.");
                continue;
            } catch (IOException e) {
                System.out.println("I/O error, please try again.");
            }
            switch (op) {
                case 1:
                    connect();
                    break;
                case 2:
                    addForest();
                    break;
                case 3:
                    addTreeSpecies();
                    break;
                case 4:
                    addSpeciesToForest();
                    break;
                case 5:
                    newWorker();
                    break;
                case 6:
                    employWorkerToState();
                    break;
                case 7:
                    placeSensor();
                    break;
                case 8:
                    generateReport();
                    break;
                case 9:
                    removeSpeciesFromForest();
                    break;
                case 10:
                    deleteWorker();
                    break;
                case 11:
                    moveSensor();
                    break;
                case 12:
                    removeWorkerFromState();
                    break;
                case 13:
                    removeSensor();
                    break;
                case 14:
                    listSensors();
                    break;
                case 15:
                    listMaintainedSensors();
                    break;
                case 16:
                    locateTreeSpecies();
                    break;
                case 17:
                    rankForestSensors();
                    break;
                case 18:
                    habitableEnvironment();
                    break;
                case 19:
                    topSensors();
                    break;
                case 20:
                    threeDegrees();
                    break;
                case 21:
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("Database access error, unsecurely terminating client...");
                            System.exit(1);
                        }
                    }
                    System.out.println("Terminating client...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Operation does not exist, please try again.");
                    break;
            }
        }
    }
}