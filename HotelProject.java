import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelProject {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/hotel_db";
    private static final String user_name = "root";
    private static final String password = "vidhya1319";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection(url, user_name, password);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nWELCOME TO OUR HOTEL");
                System.out.println("1. New Reservation");
                System.out.println("2. View Reservation");
                System.out.println("3. Update Reservation");
                System.out.println("4. Get Room Number");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Add Room");
                System.out.println("7. Exit");
                System.out.print("\nChoose one from above: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        newReservation(scanner, connection);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        updateReservation(scanner, connection);
                        break;
                    case 4:
                        getRoomNumber(scanner, connection);
                        break;
                    case 5:
                        deleteReservation(scanner, connection);
                        break;
                    case 6:
                        add_room(connection, scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Enter a valid choice.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void newReservation(Scanner scanner, Connection connection) {
        try {
            System.out.println("\nEnter the details...");
            System.out.print("Enter the guest name: ");
            String guest_name = scanner.nextLine();

            System.out.print("Enter the room number: ");
            int room_number = scanner.nextInt();
            scanner.nextLine(); 

            System.out.print("Enter the contact number: ");
            String contact_number = scanner.nextLine();

           
            String checkQuery = "SELECT availability, price FROM rooms WHERE room_number=?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, room_number);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Room number " + room_number + " does not exist.");
                return;
            }

            String availability = rs.getString("availability");
            double price = rs.getDouble("price");

            if ("Booked".equalsIgnoreCase(availability)) {
                System.out.println("Room number " + room_number + " is already booked. Choose another room.");
                return;
            }

            System.out.print("Enter number of nights: ");
            int nights = scanner.nextInt();
            scanner.nextLine();

            // Insert reservation
            String query = "INSERT INTO hotel_reservation(guest_name, room_number, conatct_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, guest_name);
            stmt.setInt(2, room_number);
            stmt.setString(3, contact_number);

            int rows_affected = stmt.executeUpdate();
            if (rows_affected > 0) {
                
                String updateRoom = "UPDATE rooms SET availability='Booked' WHERE room_number=?";
                PreparedStatement updateStmt = connection.prepareStatement(updateRoom);
                updateStmt.setInt(1, room_number);
                updateStmt.executeUpdate();
               
                double totalAmount = price * nights;
                System.out.println("\nReservation successful! Payment receipt:");
                System.out.println("Guest Name: " + guest_name);
                System.out.println("Room Number: " + room_number);
                System.out.println("Price per night: " + price);
                System.out.println("Number of nights: " + nights);
                System.out.println("Total Amount: " + totalAmount);
            } else {
                System.out.println("Reservation failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void add_room(Connection connection, Scanner scanner) {
        try {
            int room_no;
            double price;
            String Room_type;

            System.out.println("Enter the Room number:");
            room_no = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the Room type (Standard/Deluxe/Suite):");
            Room_type = scanner.nextLine();

            System.out.println("Enter the price of the room:");
            price = scanner.nextDouble();

            String query = "INSERT INTO rooms (room_number, room_type, price, availability) VALUES (?, ?, ?, 'Available')";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, room_no);
            pstmt.setString(2, Room_type);
            pstmt.setDouble(3, price);

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected > 0) {
                System.out.println("Room added successfully.");
            } else {
                System.out.println("Something went wrong!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection connection) {
        String query = "SELECT * FROM hotel_reservation;";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("\nThe details of the guest are...");
                int id = rs.getInt("reservation_id");
                String guest_name = rs.getString("guest_name");
                int roomno = rs.getInt("room_number");
                String contact = rs.getString("conatct_number");
                String time = rs.getTimestamp("reservation_date").toString();

                System.out.println("ID: " + id);
                System.out.println("NAME: " + guest_name);
                System.out.println("ROOM_NUMBER: " + roomno);
                System.out.println("CONTACT: " + contact);
                System.out.println("DATE & TIME: " + time);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getRoomNumber(Scanner scanner, Connection connection) {
        System.out.print("Enter the ID of the guest: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter the Name of the guest: ");
        String name = scanner.nextLine();

        String query = "SELECT room_number FROM hotel_reservation WHERE reservation_id=" + id + " AND guest_name='" + name + "';";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                int room_number1 = rs.getInt("room_number");
                System.out.println("The Room Number of guest " + name + " is " + room_number1);
            } else {
                System.out.println("No user matched for " + name + " and ID " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Scanner scanner, Connection connection) {
        System.out.print("Enter the id of the guest: ");
        int id1 = scanner.nextInt();

        try {
            if (reservationExists(connection, id1)) {

                
                String roomQuery = "SELECT room_number FROM hotel_reservation WHERE reservation_id=?";
                PreparedStatement roomStmt = connection.prepareStatement(roomQuery);
                roomStmt.setInt(1, id1);
                ResultSet rs = roomStmt.executeQuery();
                int room_number = 0;
                if (rs.next()) {
                    room_number = rs.getInt("room_number");
                }

                
                String query = "DELETE FROM hotel_reservation WHERE reservation_id=?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, id1);
                int rows_affected1 = stmt.executeUpdate();

                if (rows_affected1 > 0) {
                    
                    String updateRoom = "UPDATE rooms SET availability='Available' WHERE room_number=?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateRoom);
                    updateStmt.setInt(1, room_number);
                    updateStmt.executeUpdate();

                    System.out.println("Deletion completed successfully.");
                } else {
                    System.out.println("Deletion failed.");
                }
            } else {
                System.out.println("There is no reservation with ID " + id1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Scanner scanner, Connection connection) {
        System.out.print("Enter the reservation id to update: ");
        int rid = scanner.nextInt();
        scanner.nextLine(); 
        try {
            if (reservationExists(connection, rid)) {
                System.out.print("Enter the new guest Name: ");
                String guest_name = scanner.nextLine();

                System.out.print("Enter the guest's room number: ");
                int room = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter the guest's contact number: ");
                String number = scanner.nextLine();

                String query = "UPDATE hotel_reservation SET guest_name='" + guest_name + "', room_number=" + room +
                        ", conatct_number='" + number + "' WHERE reservation_id=" + rid + ";";

                Statement stmt = connection.createStatement();
                int rows_affected2 = stmt.executeUpdate(query);

                if (rows_affected2 > 0) {
                    System.out.println("Updated successfully.");
                } else {
                    System.out.println("Updation failed.");
                }
            } else {
                System.out.println("Reservation not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int id) {
        String query = "SELECT reservation_id FROM hotel_reservation WHERE reservation_id=" + id + ";";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
