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

import java.math.BigDecimal;
import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ArborDB {
    private static Connection connection; // Connection to be maintained with the database
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // For I/O
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

    private static boolean verifyConnection() {
        if (connection == null) {
            System.out.println("No connection. Please run connect command before modifying/querying the database.");
            return false;
        }
        return true;
    }

    private static void displaySensorTable(ResultSet resultSet) throws SQLException {
        // formatted table-style display
        System.out.printf("--------------------------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-9s | %-21s | %-6s | %-21s | %-9s | %-9s | %-13s |%n", "Sensor ID", "Last Charged", "Energy", "Last Read", "X", "Y", "Maintainer ID");
        System.out.printf("--------------------------------------------------------------------------------------------------------------%n");
        do {
            // take all columns and place in row
            int sensorId = resultSet.getInt("sensor_id");
            Timestamp lastCharged = resultSet.getTimestamp("last_charged");
            int energy = resultSet.getInt("energy");
            Timestamp lastRead = resultSet.getTimestamp("last_read");
            double sensorX = resultSet.getDouble("X");
            double sensorY = resultSet.getDouble("Y");
            String maintainerId = resultSet.getString("maintainer_id");
            System.out.printf("| %-9s | %-21s | %-6s | %-21s | %-9s | %-9s | %-13s |%n",
                    sensorId, lastCharged, energy, lastRead, sensorX, sensorY, maintainerId);
        } while (resultSet.next());
        // end table
        System.out.printf("--------------------------------------------------------------------------------------------------------------%n");
    }

    private static void displayForestTable(ResultSet resultSet) throws SQLException {
        // formatted table-style display
        System.out.printf("------------------------------------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-9s | %-30s | %-10s | %-10s | %-9s | %-9s | %-9s | %-9s |%n", "Forest No", "Name", "Area", "Acid Level", "XMin", "XMax", "YMin", "YMax");
        System.out.printf("------------------------------------------------------------------------------------------------------------------------%n");
        do {
            // take all columns and place in row
            int forestNo = resultSet.getInt("forest_no");
            String name = resultSet.getString("name");
            int area = resultSet.getInt("area");
            double acidLevel = resultSet.getDouble("acid_level");
            double minX = resultSet.getDouble("MBR_XMin");
            double maxX = resultSet.getDouble("MBR_XMax");
            double minY = resultSet.getDouble("MBR_YMin");
            double maxY = resultSet.getDouble("MBR_YMax");
            System.out.printf("| %-9s | %-30s | %-10s | %-10s | %-9s | %-9s | %-9s | %-9s |%n",
                    forestNo, name, area, acidLevel, minX, maxX, minY, maxY);
        } while (resultSet.next());
        // end table
        System.out.printf("------------------------------------------------------------------------------------------------------------------------%n");
    }

    private static void printGenericSQLError(SQLException e) {
        System.out.println("SQL Error");
        System.out.println("Message = " + e.getMessage());
        System.out.println("SQLState = " + e.getSQLState());
        System.out.println("SQL Code = " + e.getErrorCode());
    }

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
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=arbor_db", user, pwd);
            } catch (SQLException e) { // Thrown when, for any reason, connection cannot be established
                System.out.println("Connection error. Please make sure that the database is active and that your username and password are correct.");
                return;
            }
            System.out.println("Connection established successfully.");
        } else { // If connection already exists (not null), no need to connect
            System.out.println("Already connected to database.");
        }
    }

    private static void addForest() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL addForest(?,?,?,?,?,?,?)");
            System.out.print("Enter name: ");
            call.setString(1, br.readLine());
            System.out.print("Enter area: ");
            call.setInt(2, Integer.parseInt(br.readLine()));
            System.out.print("Enter acid level: ");
            call.setBigDecimal(3, new BigDecimal(br.readLine()));
            System.out.print("Enter minimum X value of MBR: ");
            call.setBigDecimal(4, new BigDecimal(br.readLine()));
            System.out.print("Enter maximum X value of MBR: ");
            call.setBigDecimal(5, new BigDecimal(br.readLine()));
            System.out.print("Enter minimum Y value of MBR: ");
            call.setBigDecimal(6, new BigDecimal(br.readLine()));
            System.out.print("Enter maximum Y value of MBR: ");
            call.setBigDecimal(7, new BigDecimal(br.readLine()));
            // execute call
            call.execute();
            // report to user
            System.out.println("Forest added successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                if (e.getSQLState().equals("MBRBD")) {
                    System.out.println("Bounds for forest are inverted or will produce an area of 0. Insertion cancelled.");
                } else if (e.getSQLState().equals("FOLAP")) {
                    System.out.println("Forest overlaps with existing forest. Insertion cancelled.");
                } else {
                    printGenericSQLError(e);
                }
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to main menu.");
        }
    }

    private static void addTreeSpecies() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL addTreeSpecies(?,?,?,?,?)");
            System.out.print("Enter genus: ");
            call.setString(1, br.readLine());
            System.out.print("Enter epithet: ");
            call.setString(2, br.readLine());
            System.out.print("Enter temperature: ");
            call.setBigDecimal(3, new BigDecimal(br.readLine()));
            System.out.print("Enter height: ");
            call. setBigDecimal(4, new BigDecimal(br.readLine()));
            System.out.print("Enter Raunkiaer life form specification: ");
            call.setString(5, br.readLine());
            // execute call
            call.execute();
            // report to user
            System.out.println("Tree species added successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {

                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to main menu.");
        }
    }

    private static void addSpeciesToForest() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL addSpeciesToForest(?,?,?)");
            System.out.print("Enter forest no: ");
            call.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter genus: ");
            call.setString(2, br.readLine());
            System.out.print("Enter epithet: ");
            call.setString(3, br.readLine());
            // execute call
            call.execute();
            // report to user
            System.out.println("Species added to forest successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
        }
    }

    private static void newWorker() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL newWorker(?,?,?,?,?,?)");
            System.out.print("Enter SSN: ");
            call.setString(1, br.readLine());
            System.out.print("Enter first name: ");
            call.setString(2, br.readLine());
            System.out.print("Enter last name: ");
            call.setString(3, br.readLine());
            System.out.print("Enter middle initial: ");
            call.setString(4, br.readLine());
            System.out.print("Enter rank: ");
            call.setString(5, br.readLine());
            System.out.print("Enter state abbreviation: ");
            call.setString(6, br.readLine());
            // execute call
            call.execute();
            // report to user
            System.out.println("Worker added successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
        }
    }

    private static void employWorkerToState() {
          try {
              // verify connection, return if not established
              if (!verifyConnection()) return;
              // prepare SQL call
              CallableStatement call = connection.prepareCall("CALL employWorkerToState(?,?)");
              System.out.print("Enter state abbreviation: ");
              call.setString(1, br.readLine());
              System.out.print("Enter worker SSN: ");
              call.setString(2, br.readLine());
              // execute call
              call.execute();
              // report to user
              System.out.println("Worker employed to state successfully.");
          // handle SQL exceptions
          } catch (SQLException e) {
              while (e != null) {
                  printGenericSQLError(e);
                  e = e.getNextException();
              }
          // handle I/O exceptions
          } catch (IOException e) {
              System.out.println("I/O error, returning to the main menu.");
          // handle format exceptions
          } catch (NumberFormatException e) {
              System.out.println("The provided input is invalid, returning to the main menu.");
          }
    }

    private static void placeSensor() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // preparse SQL call
            CallableStatement call = connection.prepareCall("CALL placeSensor(?,?,?,?)");
            System.out.print("Enter energy: ");
            call.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter X location: ");
            call.setDouble(2, Double.parseDouble(br.readLine()));
            System.out.print("Enter Y location: ");
            call.setDouble(3, Double.parseDouble(br.readLine()));
            System.out.print("Enter maintainer ID: ");
            call.setString(4, br.readLine());
            // execute SQL call
            call.execute();
            // report to user
            System.out.println("Sensor placed successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            // if NOEMP state, sensor cannot be placed
            if (e.getSQLState().equals("NOEMP")) {
                System.out.println("Sensor location is outside maintainer's jurisdiction. Operation cancelled.");
                // otherwise, report general error
            } else {
                printGenericSQLError(e);
            }
            e = e.getNextException();
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
        }
    }

    private static void generateReport() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // fetch all sensors
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SENSOR ORDER BY sensor_id");
            ResultSet resultSet = stmt.executeQuery();
            // if there are no sensors in the db, inform the user and return
            if (!resultSet.next()) {
                System.out.println("No sensors currently deployed.");
                return;
            }
            // otherwise, display all sensors
            System.out.println("Below is a list of all sensors in the database:");
            displaySensorTable(resultSet);
            // prompt the user to choose one to generate a report
            System.out.print("Enter the ID of the sensor that will generate the report, or enter -1 to cancel: ");
            int sensorId = Integer.parseInt(br.readLine());
            // if sensorId is -1, exit
            if (sensorId == -1)
                return;
            // otherwise, prepare SQL call
            CallableStatement call = connection.prepareCall("CALL generateReport(?,?,?)");
            call.setInt(1, sensorId);
            System.out.print("Enter report time (format: yyyy-MM-dd HH:mm:ss.SSSSSS): ");
            call.setTimestamp(2, new Timestamp(dateFormat.parse(br.readLine()).getTime()));
            System.out.print("Enter recorded temperature: ");
            call.setBigDecimal(3, new BigDecimal(br.readLine()));
            // execute call
            call.execute();
            // report to user
            System.out.println("Report generated successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
        // handle parse exception
        } catch (ParseException e) {
            System.out.println("Provided timestamp is invalid, returning to the main menu.");
        }
    }

    private static void removeSpeciesFromForest() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL removeSpeciesFromForest(?,?,?)");
            System.out.print("Enter forest no: ");
            call.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter genus: ");
            call.setString(2, br.readLine());
            System.out.print("Enter epithet: ");
            call.setString(3, br.readLine());
            // execute call
            call.execute();
            // report to user
            System.out.println("Species removed successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
        }
    }

    private static void deleteWorker() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL deleteWorker(?)");
            System.out.print("Enter worker SSN: ");
            String ssn = br.readLine();
            call.setString(1, ssn);
            // execute call
            call.execute();
            // report to user
            System.out.println("Worker with SSN " + ssn + " deleted successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        }
        
    }

    private static void moveSensor() {
         try {
             // verify connection, return if not established
             if (!verifyConnection()) return;
             // fetch all sensors
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SENSOR ORDER BY sensor_id");
             ResultSet resultSet = stmt.executeQuery();
             // if there are no sensors in the db, inform the user and return
             if (!resultSet.next()) {
                 System.out.println("No sensors currently deployed.");
                 return;
             }
             // otherwise, ask user for sensor to move
             System.out.print("Enter ID of sensor to move, or enter -1 to cancel: ");
             int sensorId = Integer.parseInt(br.readLine());
             // if sensorId is -1, exit
             if (sensorId == -1)
                 return;
             // otherwise, assemble call
             CallableStatement call = connection.prepareCall("CALL moveSensor(?,?,?)");
             call.setInt(1, sensorId);
             System.out.print("Enter new X location: ");
             call.setBigDecimal(2, new BigDecimal(br.readLine()));
             System.out.print("Enter new Y location: ");
             call.setBigDecimal(3, new BigDecimal(br.readLine()));
             // execute call
             call.execute();
             // report to user
             System.out.println("Sensor successfully moved to new location.");
         // handle SQL exceptions
         } catch (SQLException e) {
             while (e != null) {
                 // if NOEMP state, sensor could not be moved
                 if (e.getSQLState().equals("NOEMP")) {
                     System.out.println("New location is outside maintainer's jurisdiction. Operation cancelled.");
                 // otherwise, report general error
                 } else {
                     printGenericSQLError(e);
                 }
                 e = e.getNextException();
             }
         } catch (IOException e) {
             System.out.println("I/O error, returning to the main menu.");
         } catch (NumberFormatException e) {
             System.out.println("The provided input is invalid, returning to the main menu.");
         }
    }

    private static void removeWorkerFromState() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            CallableStatement call = connection.prepareCall("CALL removeWorkerFromState(?,?)");
            System.out.print("Enter worker SSN: ");
            String ssn = br.readLine();
            call.setString(1, ssn);
            System.out.print("Enter state abbreviation: ");
            String stateAbb = br.readLine();
            call.setString(2, stateAbb);
            // execute call
            call.execute();
            // report to user
            System.out.println("Worker " + ssn + " removed from state " + stateAbb + " successfully.");
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        }
        
    }

    private static void removeSensor() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // fetch all sensors
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM SENSOR ORDER BY sensor_id");

            // if there are no sensors in the db, inform the user and return
            if (!resultSet.next()) {
                System.out.println("No sensors currently deployed.");
                return;
            }
            // otherwise, ask user if they want to remove all or select sensors
            System.out.print("Would you like to remove 'all' sensors or 'select' sensors for removal? ");
            String c = br.readLine().toLowerCase();
            // if user chooses all
            if (c.equals("all")) {
                // ask for confirmation
                System.out.print("Are you sure? Enter 'yes' to confirm or 'no' to cancel: ");
                String c2 = br.readLine().toLowerCase();
                // if confirmed, remove all sensors
                if (c2.equals("yes")) {
                    CallableStatement call = connection.prepareCall("DELETE FROM SENSOR");
                    call.execute();
                    System.out.println("All sensors removed.");
                // if not confirmed, return to menu
                } else if (c2.equals("no")) {
                    System.out.println("No sensors were removed.");
                // if invalid response, return to menu
                } else {
                    System.out.println("Invalid response. Returning to main menu.");
                }
            // if user chooses select
            } else if (c.equals("select")) {
                int d = 0;
                do {
                    // formatted table-style display
                    System.out.printf("--------------------------------------------------------------------------------------------------------------%n");
                    System.out.printf("| %-9s | %-21s | %-6s | %-21s | %-9s | %-9s | %-13s |%n", "Sensor ID", "Last Charged", "Energy", "Last Read", "X", "Y", "Maintainer ID");
                    System.out.printf("--------------------------------------------------------------------------------------------------------------%n");
                    // display column data
                    int sensorId = resultSet.getInt("sensor_id");
                    Timestamp lastCharged = resultSet.getTimestamp("last_charged");
                    int energy = resultSet.getInt("energy");
                    Timestamp lastRead = resultSet.getTimestamp("last_read");
                    double sensorX = resultSet.getDouble("X");
                    double sensorY = resultSet.getDouble("Y");
                    String maintainerId = resultSet.getString("maintainer_id");
                    System.out.printf("| %-9s | %-21s | %-6s | %-21s | %-9s | %-9s | %-13s |%n",
                            sensorId, lastCharged, energy, lastRead, sensorX, sensorY, maintainerId);
                    // end table
                    System.out.printf("--------------------------------------------------------------------------------------------------------------%n");
                    // prompt user to enter sensorId for confirmation
                    System.out.println("Enter sensor ID to remove, 0 to skip, or -1 to return to menu.");
                    try {
                        d = Integer.parseInt(br.readLine());
                    // if input is not in integer format, warn user and decrement resultSet, then advance
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter the sensor ID, 0, or -1.");
                        resultSet.previous();
                        continue;
                    }
                    // if input matches sensor id
                    if (d == sensorId) {
                        // prep call
                        CallableStatement call = connection.prepareCall("CALL removeSensor(?)");
                        call.setInt(1, sensorId);
                        // execute call
                        call.execute();
                        // inform user
                        System.out.println("Sensor removed.");
                    // if input is 0, move forward
                    } else if (d == 0) {
                        continue;
                    // if input is -1, exit to menu
                    } else if (d == -1) {
                        return;
                    // if input is none of these, tell the user and move the set backwards
                    } else {
                        System.out.println("Please enter the sensor ID, 0, or -1.");
                        resultSet.previous();
                    }
                // stop when list of sensors ends
                } while (resultSet.next());
            } else {
                System.out.println("Invalid response. Returning to main menu.");
            }
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
        // handle format exceptions
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
        }
    }

    private static void listSensors() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM listSensors(?)");
            System.out.print("Enter Forest No: ");
            stmt.setInt(1, Integer.parseInt(br.readLine()));
            // put results in resultSet
            ResultSet resultSet = stmt.executeQuery();
            // if no results, inform user
            if (!resultSet.next()) {
                System.out.println("No sensors found in the specified forest.");
            // otherwise, display results
            } else {
                System.out.println("Sensors in specified forest:");
                displaySensorTable(resultSet);
            }
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input or I/O error, returning to the main menu.");
            return;
        }
        
    }

    private static void listMaintainedSensors() {
         try {
             // verify connection, return if not established
             if (!verifyConnection()) return;
             // prepare SQL call
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM listMaintainedSensors(?)");
             System.out.print("Enter Worker SSN: ");
             stmt.setString(1, br.readLine());
             // put results in resultSet
             ResultSet resultSet = stmt.executeQuery();
             // if no results, inform user
             if (!resultSet.next()) {
                 System.out.println("No sensors maintained by given worker.");
             // otherwise, display results
             } else {
                 System.out.println("Below are the sensors maintained by the given worker:");
                 displaySensorTable(resultSet);
             }
         // handle SQL exceptions
         } catch (SQLException e) {
             while (e != null) {
                 printGenericSQLError(e);
                 e = e.getNextException();
             }
         // handle I/O exceptions
         } catch (IOException | NumberFormatException e) {
             System.out.println("Invalid input or I/O error, returning to the main menu.");
         }
    }

    private static void locateTreeSpecies() {
         try {
             // verify connection, return if not established
             if (!verifyConnection()) return;
             // prepare SQL call
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM locateTreeSpecies(?, ?)");
             System.out.print("Enter Genus pattern: ");
             stmt.setString(1, br.readLine());
             System.out.print("Enter Epithet pattern: ");
             stmt.setString(2, br.readLine());
             // put results in resultSet
             ResultSet resultSet = stmt.executeQuery();
             // if no results, inform user
             if (!resultSet.next()) {
                 System.out.println("No forests found with the specified tree species pettern.");
             // otherwise, display results
             } else {
                 System.out.println("Forests with tree species matching the given pattern:");
                 displayForestTable(resultSet);
             }
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to main menu.");
            return;
        }

    }

    private static void rankForestSensors() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rankForestSensors()");
            // put results in resultSet
            ResultSet resultSet = stmt.executeQuery();
            // if no results, inform user
            if (!resultSet.next()) {
                System.out.println("No forests to rank.");
            // otherwise, display results
            } else {
                System.out.println("Below are the forests ranked on number of sensors:");
                // formatted table-style display
                System.out.printf("--------------------%n");
                System.out.printf("| %-4s | %-9s |%n", "Rank", "Forest No");
                System.out.printf("--------------------%n");
                do {
                    // take all columns and place in row
                    int rank = resultSet.getInt("rank");
                    int forestNo = resultSet.getInt("forest_no");
                    System.out.printf("| %-4s | %-9s |%n", rank, forestNo);
                } while (resultSet.next());
                // end table
                System.out.printf("--------------------%n");
            }
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        }
    }

    private static void habitableEnvironment() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM habitableEnvironment(?,?,?)");
            System.out.print("Enter Genus: ");
            stmt.setString(1, br.readLine());
            System.out.print("Enter Epithet: ");
            stmt.setString(2, br.readLine());
            System.out.print("Enter k (years from present to consider): ");
            stmt.setInt(3, Integer.parseInt(br.readLine()));
            // put results in resultSet
            ResultSet resultSet = stmt.executeQuery();
            // if no results, inform user
            if (!resultSet.next()) {
                System.out.println("No habitable environments were found for the given species.");
            // otherwise, display results
            } else {
                System.out.println("Habitable forests for the given species:");
                displayForestTable(resultSet);
            }
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException | NumberFormatException e) {
            System.out.println("Input Error");
        }
    }

    private static void topSensors() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM topSensors(?,?)");
            System.out.print("Enter k (number of sensors): ");
            stmt.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter x (months back from present to consider): ");
            stmt.setInt(2, Integer.parseInt(br.readLine()));
            // put results in resultSet
            ResultSet resultSet = stmt.executeQuery();
            // if no results, inform user
            if (!resultSet.next()) {
                System.out.println("No sensors found.");
            // otherwise, display results
            } else {
                System.out.println("Below are the top sensors:");
                displaySensorTable(resultSet);
            }
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException | NumberFormatException e) {
            System.out.println("Input Error");
        }
    }

    private static void threeDegrees() {
        try {
            // verify connection, return if not established
            if (!verifyConnection()) return;
            // prepare SQL call
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM threeDegrees(?,?)");
            System.out.print("Enter first Forest No (f1): ");
            stmt.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter second Forest No (f2): ");
            stmt.setInt(2, Integer.parseInt(br.readLine()));
            // put results in resultSet
            ResultSet rslt = stmt.executeQuery();
            // if no results, inform user
            if (rslt.next()) {
                System.out.println("Path between forests: " + rslt.getString(1));
            // otherwise, display results
            } else {
                System.out.println("No three-hop path exists between these forests.");
            }
        // handle SQL exceptions
        } catch (SQLException e) {
            while (e != null) {
                printGenericSQLError(e);
                e = e.getNextException();
            }
        // handle I/O exceptions
        } catch (IOException | NumberFormatException e) {
            System.out.println("Input Error");
        }
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
