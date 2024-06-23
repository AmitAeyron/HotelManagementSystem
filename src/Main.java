
import java.sql.*;
import java.util.Scanner;


public class Main
{
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "Amit@123");
            while (true) {
                System.out.println();
                System.out.println("Hotel Management System");
                Scanner scanner = new Scanner(System.in);

                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Choose an option");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        return;
                    case 4:
                        updateReservation(connection, scanner);
                        return;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again");
                }

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private static void reserveRoom(Connection connection, Scanner scanner){

        try{
            System.out.println("Enter guest name: ");
            String guestName=scanner.next();
            scanner.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber= scanner.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber=scanner.next();

            String sql="Insert into reservations(guest_name,room_number,contact_number) "+"values ('"+guestName+ "','" + roomNumber+ "','"+contactNumber+"')";

            try(Statement statement=connection.createStatement()){
                int affactrows=statement.executeUpdate(sql);

                if(affactrows>0){
                    System.out.println("Reservation Successful!");
                }
                else{
                    System.out.println("Reservation failed.");
                }
            }

        }catch (SQLException e){
              e.printStackTrace();
        }
    }
    private static void viewReservation(Connection connection) throws SQLException{
            String sql="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";

            try{
                Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(sql);


                System.out.println("Current Reservations");


                while(resultSet.next()){
                    int reservationId=resultSet.getInt("reservation_id");
                    String guestName=resultSet.getString("guest_name");
                    int roomNumber=resultSet.getInt("room_number");
                    String contactNumber=resultSet.getString("contact_number");
                    String reservationDate=resultSet.getTimestamp("reservation_date").toString();

                    System.out.println(reservationId+" "+ guestName+" "+ roomNumber+" "+contactNumber+" "+ reservationDate);
                    System.out.println("+-----------------+-----------------+----------------+");
                }

            }catch(Exception e){
                e.getMessage();

            }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner){
        System.out.println("Enter reservation ID: ");
        int reservationId=scanner.nextInt();
        System.out.println("Enter guest name: ");
        String guestName= scanner.next();

        String sql="select room_number from reservations "+"where reservation_id="+reservationId+" and guest_name= " +guestName;

        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);

            if(resultSet.next()){
                int roomNumber=resultSet.getInt("room_number");
                System.out.println("Room number for Reservation ID "+ reservationId + " and guest " + guestName+" is: "+ roomNumber);
            }

        }catch (Exception e){
            e.getMessage();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner){

       try{
          System.out.println("Enter registration ID to update: ");
          int reservationId=scanner.nextInt();
          scanner.nextLine();   //Consume the newline

           if(!reservationExists(connection,reservationId)){
               System.out.println("Reservation not found for the given ID.");
           }


           System.out.println("Enter new guest name: ");
           String newGuestName=scanner.nextLine();

           System.out.println("Enter new room number");
           int newRoomNumber=scanner.nextInt();

           System.out.println("Enter the new contact number");
           String newContactNumber=scanner.next();

           String sql="update reservations set guest_name="+newGuestName + ", " + "room_number= "+newRoomNumber+ ","+ "contact_number= "+newContactNumber+  " where reservation_id="+ reservationId ;



           try{
               Statement statement=connection.createStatement();
               int affactrow=statement.executeUpdate(sql);

               if(affactrow>0){
                   System.out.println("Reservaion updated successfully");
               }else{
                   System.out.println("Reservation upation failed");
               }

           }catch (Exception e){
               e.printStackTrace();
           }



      }catch(Exception e){
        e.getMessage();
       }
   }
//     private static void deletReservation(Connection connection,Scanner scanner){
//
//        try{
//            System.out.println("Enter reservation ID: ");
//            int reservationId=scanner.nextInt();
//            scanner.nextLine();
//
//            if(!reservationExists(connection,reservationId)){
//                System.out.println("Reservation not found for the given ID.");
//            }
//
//            String sql="delete from reservations where reservations_id="+ reservationId;
//
//            try(
//                Statement statement=connection.createStatement()){
//                int rowaffected=statement.executeUpdate(sql);
//
//                if(rowaffected>0){
//                    System.out.println("Reservation deleted successfully!.");
//                }else{
//                    System.out.println("Reservation deletion failed.");
//                }
//            }catch (SQLException e){
//                e.getMessage();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//     }

    private static void deleteReservation(Connection con,Scanner sc)
    {
        System.out.println("enter reservation id to delete record");
        int resdel= sc.nextInt();
        sc.nextLine();
        if (!reservationExists(con , resdel))
        {
            System.out.println("Reservation not found");
            return;
        }
        String sql="DELETE FROM reservations WHERE reservation_id =" +resdel;
        try {
            Statement sts = con.createStatement();
            int rowaff = sts.executeUpdate(sql);
            if (rowaff > 0) {
                System.out.println("reservation deleted");
            } else {
                System.out.println("deletetion failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
      private static boolean reservationExists(Connection connection,int reservationId) {

          try {

              String sql="select reservation_id from reservations where reservation_id= "+reservationId;

              try{
                  Statement statement=connection.createStatement();
                  ResultSet resultSet=statement.executeQuery(sql);

                  return resultSet.next();

              }catch (SQLException e){
                  e.getMessage();
              }
          }catch (Exception e) {
             e.printStackTrace();
          }

          return  false;

      }

      public static void exit() throws InterruptedException{
          System.out.println("Exiting System.");
          System.out.println("Thankyou for using Hotel Reservation System");
      }

}