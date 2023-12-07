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

    private static boolean verifyConnection() {
        if (connection == null) {
            System.out.println("No connection. Please run connect command before modifying/querying the database.");
            return false;
        }
        return true;
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
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call addForest( ?,?,?,?,?,?,? ) }");
            System.out.print("Enter name: ");
            call.setString(1, br.readLine());
            System.out.print("Enter area: ");
            call.setInt(2, Integer.parseInt(br.readLine()));
            System.out.print("Enter acid level: ");
            call.setDouble(3, Double.parseDouble(br.readLine()));
            System.out.print("Enter minimum X value of MBR: ");
            call.setDouble(4, Double.parseDouble(br.readLine()));
            System.out.print("Enter maximum X value of MBR: ");
            call.setDouble(5, Double.parseDouble(br.readLine()));
            System.out.print("Enter minimum Y value of MBR: ");
            call.setDouble(6, Double.parseDouble(br.readLine()));
            System.out.print("Enter maximum Y value of MBR: ");
            call.setDouble(7, Double.parseDouble(br.readLine()));
            call.execute();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to main menu.");
            return;
        }
    }

    private static void addTreeSpecies() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call addTreeSpecies( ?,?,?,?,? ) }");
            System.out.print("Enter genus: ");
            call.setString(1, br.readLine());
            System.out.print("Enter epithet: ");
            call.setString(2, br.readLine());
            System.out.print("Enter temperature: ");
            call.setDouble(3, Double.parseDouble(br.readLine()));
            System.out.print("Enter height: ");
            call.setDouble(4, Double.parseDouble(br.readLine()));
            System.out.print("Enter Raunkiaer life form specification: ");
            call.setString(5, br.readLine());
            call.execute();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to main menu.");
            return;
        }
    }

    private static void addSpeciesToForest() {
    try {
        if (!verifyConnection()) return;
        CallableStatement call = connection.prepareCall("{ call addSpeciesToForest(?,?,?) }");
        System.out.print("Enter forest_no: ");
        call.setInt(1, Integer.parseInt(br.readLine()));
        System.out.print("Enter genus: ");
        call.setString(2, br.readLine());
        System.out.print("Enter epithet: ");
        call.setString(3, br.readLine());
        call.execute();
        System.out.println("Species added to forest successfully.");
    } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
                }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }
    }


    private static void newWorker() {

     try {
         if (!verifyConnection()) return;
        CallableStatement call = connection.prepareCall("{ call newWorker(?,?,?,?,?,?) }");
        System.out.print("Enter SSN: ");
        call.setString(1, br.readLine());
        System.out.print("Enter First name: ");
        call.setString(2, br.readLine());
        System.out.print("Enter Last name: ");
        call.setString(3, br.readLine());
        System.out.print("Enter Middle initial: ");
        call.setString(4, br.readLine());
        System.out.print("Enter Rank: ");
        call.setString(5, br.readLine());
        System.out.print("Enter State abbreviation: ");
        call.setString(6, br.readLine());
        call.execute();
        System.out.println("Worker added successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }

}
        
   

    private static void employWorkerToState() {
          try {
              if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call employWorkerToState(?,?) }");
            System.out.print("Enter State abbreviation: ");
            call.setString(1, br.readLine());
            System.out.print("Enter Worker SSN: ");
            call.setString(2, br.readLine());
            call.execute();
            System.out.println("Worker employed to state successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }
     
    }

    private static void placeSensor() {
         try {
             if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call newWorker(?,?,?,?,?,?) }");
            System.out.print("Enter SSN: ");
            call.setString(1, br.readLine());
            System.out.print("Enter First name: ");
            call.setString(2, br.readLine());
            System.out.print("Enter Last name: ");
            call.setString(3, br.readLine());
            System.out.print("Enter Middle initial: ");
            call.setString(4, br.readLine());
            System.out.print("Enter Rank: ");
            call.setString(5, br.readLine());
            System.out.print("Enter State abbreviation: ");
            call.setString(6, br.readLine());
            call.execute();
            System.out.println("Worker added successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }
        
    }

    private static void generateReport() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call placeSensor(?,?,?,?) }");
            System.out.print("Enter Energy: ");
            call.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter X Location: ");
            call.setDouble(2, Double.parseDouble(br.readLine()));
            System.out.print("Enter Y Location: ");
            call.setDouble(3, Double.parseDouble(br.readLine()));
            System.out.print("Enter Maintainer ID: ");
            call.setString(4, br.readLine());
            call.execute();
            System.out.println("Sensor placed successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }
        
    }

    private static void removeSpeciesFromForest() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call placeSensor(?,?,?,?) }");
            System.out.print("Enter Energy: ");
            call.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter X Location: ");
            call.setDouble(2, Double.parseDouble(br.readLine()));
            System.out.print("Enter Y Location: ");
            call.setDouble(3, Double.parseDouble(br.readLine()));
            System.out.print("Enter Maintainer ID: ");
            call.setString(4, br.readLine());
            call.execute();
            System.out.println("Sensor placed successfully.");
         } catch (SQLException e) {
                System.out.println("SQL Error");
                while (e != null) {
                    System.out.println("Message = " + e.getMessage());
                    System.out.println("SQLState = " + e.getSQLState());
                    System.out.println("SQL Code = " + e.getErrorCode());
                    e = e.getNextException();
                }
         } catch (IOException e) {
                System.out.println("I/O error, returning to the main menu.");
                return;
         } catch (NumberFormatException e) {
                System.out.println("The provided input is invalid, returning to the main menu.");
                return;
        }
            
    }

    private static void deleteWorker() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call deleteWorker(?) }");
            System.out.print("Enter Worker SSN: ");
            String ssn = br.readLine();
            call.setString(1, ssn);
    
            call.execute();
            System.out.println("Worker with SSN " + ssn + " deleted successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        }
        
    }

    private static void moveSensor() {
         try {
             if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call moveSensor(?,?,?) }");
            System.out.print("Enter Sensor ID: ");
            int sensorId = Integer.parseInt(br.readLine());
            call.setInt(1, sensorId);
            
            System.out.print("Enter new X location: ");
            double newX = Double.parseDouble(br.readLine());
            call.setDouble(2, newX);
            
            System.out.print("Enter new Y location: ");
            double newY = Double.parseDouble(br.readLine());
            call.setDouble(3, newY);
    
            call.execute();
            System.out.println("Sensor " + sensorId + " moved to new location successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }
        
    }

    private static void removeWorkerFromState() {

        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call removeWorkerFromState(?,?) }");
            System.out.print("Enter Worker SSN: ");
            String ssn = br.readLine();
            call.setString(1, ssn);
            
            System.out.print("Enter State Abbreviation: ");
            String stateAbb = br.readLine();
            call.setString(2, stateAbb);
    
            call.execute();
            System.out.println("Worker " + ssn + " removed from state " + stateAbb + " successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        }
        
    }

    private static void removeSensor() {

        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call removeSensor(?) }");
            
            System.out.print("Do you want to remove all sensors? (yes/no): ");
            String response = br.readLine().toLowerCase();
            
            if (response.equals("yes")) {
                call.setNull(1, Types.INTEGER);  // Null parameter to indicate removing all sensors
            } else if (response.equals("no")) {
                System.out.print("Enter Sensor ID to remove: ");
                int sensorId = Integer.parseInt(br.readLine());
                call.setInt(1, sensorId);
            } else {
                System.out.println("Invalid response. Returning to the main menu.");
                return;
            }
    
            call.execute();
            
            if (response.equals("yes")) {
                System.out.println("All sensors removed successfully.");
            } else {
                System.out.println("Sensor removed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to the main menu.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("The provided input is invalid, returning to the main menu.");
            return;
        }
            
    }

    private static void listSensors() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call listSensors(?) }");
    
            System.out.print("Enter Forest ID: ");
            int forestId = Integer.parseInt(br.readLine());
            call.setInt(1, forestId);
    
            ResultSet resultSet = call.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("No sensors found in the specified forest.");
            } else {
                System.out.println("Sensors in Forest " + forestId + ":");
                do {
                    int sensorId = resultSet.getInt("sensor_id");
                    Timestamp lastCharged = resultSet.getTimestamp("last_charged");
                    int energy = resultSet.getInt("energy");
                    Timestamp lastRead = resultSet.getTimestamp("last_read");
                    double x = resultSet.getDouble("X");
                    double y = resultSet.getDouble("Y");
                    String maintainerId = resultSet.getString("maintainer_id");
    
                    System.out.println("Sensor ID: " + sensorId +
                            ", Last Charged: " + lastCharged +
                            ", Energy: " + energy +
                            ", Last Read: " + lastRead +
                            ", X: " + x +
                            ", Y: " + y +
                            ", Maintainer ID: " + maintainerId);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input or I/O error, returning to the main menu.");
            return;
        }
        
    }

    private static void listMaintainedSensors() {
         try {
             if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call listSensors(?) }");
    
            System.out.print("Enter Forest ID: ");
            int forestId = Integer.parseInt(br.readLine());
            call.setInt(1, forestId);
    
            ResultSet resultSet = call.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("No sensors found in the specified forest.");
            } else {
                System.out.println("Sensors in Forest " + forestId + ":");
                do {
                    int sensorId = resultSet.getInt("sensor_id");
                    Timestamp lastCharged = resultSet.getTimestamp("last_charged");
                    int energy = resultSet.getInt("energy");
                    Timestamp lastRead = resultSet.getTimestamp("last_read");
                    double x = resultSet.getDouble("X");
                    double y = resultSet.getDouble("Y");
                    String maintainerId = resultSet.getString("maintainer_id");
    
                    System.out.println("Sensor ID: " + sensorId +
                            ", Last Charged: " + lastCharged +
                            ", Energy: " + energy +
                            ", Last Read: " + lastRead +
                            ", X: " + x +
                            ", Y: " + y +
                            ", Maintainer ID: " + maintainerId);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input or I/O error, returning to the main menu.");
            return;
        }
        
    }

    private static void locateTreeSpecies() {
         try {
             if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call locateTreeSpecies(?, ?) }");
    
            System.out.print("Enter Genus pattern: ");
            String genusPattern = br.readLine();
            System.out.print("Enter Epithet pattern: ");
            String epithetPattern = br.readLine();
    
            call.setString(1, genusPattern);
            call.setString(2, epithetPattern);
    
            ResultSet resultSet = call.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("No forests found with the specified tree species patterns.");
            } else {
                System.out.println("Forests with Tree Species matching patterns:");
                do {
                    int forestNo = resultSet.getInt("forest_no");
                    String name = resultSet.getString("name");
                    int area = resultSet.getInt("area");
                    double acidLevel = resultSet.getDouble("acid_level");
                    double minX = resultSet.getDouble("MBR_XMin");
                    double maxX = resultSet.getDouble("MBR_XMax");
                    double minY = resultSet.getDouble("MBR_YMin");
                    double maxY = resultSet.getDouble("MBR_YMax");
    
                    System.out.println("Forest No: " + forestNo +
                            ", Name: " + name +
                            ", Area: " + area +
                            ", Acid Level: " + acidLevel +
                            ", MBR XMin: " + minX +
                            ", MBR XMax: " + maxX +
                            ", MBR YMin: " + minY +
                            ", MBR YMax: " + maxY);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println("I/O error, returning to main menu.");
            return;
        }
        
    }

    private static void rankForestSensors() {
        try {
            if (!verifyConnection()) return;
        CallableStatement call = connection.prepareCall("{ call rankForestSensors() }");

        ResultSet resultSet = call.executeQuery();

        if (!resultSet.next()) {
            System.out.println("No forests to rank.");
        } else {
            System.out.println("Ranked Forests based on the number of sensors:");

            do {
                int forestNo = resultSet.getInt("forest_no");
                String name = resultSet.getString("name");
                int area = resultSet.getInt("area");
                double acidLevel = resultSet.getDouble("acid_level");
                double minX = resultSet.getDouble("MBR_XMin");
                double maxX = resultSet.getDouble("MBR_XMax");
                double minY = resultSet.getDouble("MBR_YMin");
                double maxY = resultSet.getDouble("MBR_YMax");

                System.out.println("Forest No: " + forestNo +
                        ", Name: " + name +
                        ", Area: " + area +
                        ", Acid Level: " + acidLevel +
                        ", MBR XMin: " + minX +
                        ", MBR XMax: " + maxX +
                        ", MBR YMin: " + minY +
                        ", MBR YMax: " + maxY);
            } while (resultSet.next());
        }
      } catch (SQLException e) {
        System.out.println("SQL Error");
        while (e != null) {
            System.out.println("Message = " + e.getMessage());
            System.out.println("SQLState = " + e.getSQLState());
            System.out.println("SQL Code = " + e.getErrorCode());
            e = e.getNextException();
        }
     }

        
    }

    private static void habitableEnvironment() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call habitableEnvironment(?,?,?) }");
    
            System.out.print("Enter genus: ");
            String genus = br.readLine();
            System.out.print("Enter epithet: ");
            String epithet = br.readLine();
            System.out.print("Enter k: ");
            int k = Integer.parseInt(br.readLine());
    
            call.setString(1, genus);
            call.setString(2, epithet);
            call.setInt(3, k);
    
            ResultSet resultSet = call.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("No habitable environments were found.");
            } else {
                System.out.println("Habitable Environments for the Tree Species:");
    
                do {
                    int forestNo = resultSet.getInt("forest_no");
                    String name = resultSet.getString("name");
                    int area = resultSet.getInt("area");
                    double acidLevel = resultSet.getDouble("acid_level");
                    double minX = resultSet.getDouble("MBR_XMin");
                    double maxX = resultSet.getDouble("MBR_XMax");
                    double minY = resultSet.getDouble("MBR_YMin");
                    double maxY = resultSet.getDouble("MBR_YMax");
    
                    System.out.println("Forest No: " + forestNo +
                            ", Name: " + name +
                            ", Area: " + area +
                            ", Acid Level: " + acidLevel +
                            ", MBR XMin: " + minX +
                            ", MBR XMax: " + maxX +
                            ", MBR YMin: " + minY +
                            ", MBR YMax: " + maxY);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Input Error");
        }
        
    }

    private static void topSensors() {
        try {
            if (!verifyConnection()) return;
            CallableStatement call = connection.prepareCall("{ call topSensors(?,?) }");
    
            System.out.print("Enter k: ");
            int k = Integer.parseInt(br.readLine());
            System.out.print("Enter x: ");
            int x = Integer.parseInt(br.readLine());
    
            call.setInt(1, k);
            call.setInt(2, x);
    
            ResultSet resultSet = call.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("No sensors found.");
            } else {
                System.out.println("Top Sensors:");
    
                do {
                    int sensorId = resultSet.getInt("sensor_id");
                    Timestamp lastCharged = resultSet.getTimestamp("last_charged");
                    int energy = resultSet.getInt("energy");
                    Timestamp lastRead = resultSet.getTimestamp("last_read");
                    double sensorX = resultSet.getDouble("X");
                    double sensorY = resultSet.getDouble("Y");
                    String maintainerId = resultSet.getString("maintainer_id");
    
                    System.out.println("Sensor ID: " + sensorId +
                            ", Last Charged: " + lastCharged +
                            ", Energy: " + energy +
                            ", Last Read: " + lastRead +
                            ", X: " + sensorX +
                            ", Y: " + sensorY +
                            ", Maintainer ID: " + maintainerId);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Input Error");
        }
            
    }

    private static void threeDegrees() {
        try {
            if (!verifyConnection()) return;
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM threeDegrees(?,?)");
            System.out.print("Enter first forest number (f1): ");
            stmt.setInt(1, Integer.parseInt(br.readLine()));
            System.out.print("Enter second forest number (f2): ");
            stmt.setInt(2, Integer.parseInt(br.readLine()));
            ResultSet rslt = stmt.executeQuery();
        if (rslt.next()) {
            System.out.println("Path between forests: " + rslt.getString(1));
        } else {
            System.out.println("No three-hop path exists between these forests.");
        }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            while (e != null) {
                System.out.println("Message = " + e.getMessage());
                System.out.println("SQLState = " + e.getSQLState());
                System.out.println("SQL Code = " + e.getErrorCode());
                e = e.getNextException();
            }
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
