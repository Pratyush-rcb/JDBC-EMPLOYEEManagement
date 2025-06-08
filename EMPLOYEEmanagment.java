import java.sql.*;
import java.util.Scanner;

public class EMPLOYEEmanagment {
    static final String DB_URL = "jdbc:mysql://localhost:3306/students";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            while (true) {
                System.out.println("\n==== Employee Management ====");
                System.out.println("1. Add Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Update Employee Name");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();

                        String insertSQL = "INSERT INTO employees (id, name) VALUES (?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                        insertStmt.setInt(1, id);
                        insertStmt.setString(2, name);
                        insertStmt.executeUpdate();
                        System.out.println("✅ Employee added.");
                        insertStmt.close();
                        break;

                    case 2:
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

                        System.out.println("\n📋 Employee List:");
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
                        }
                        rs.close();
                        stmt.close();
                        break;

                    case 3:
                        System.out.print("Enter Employee ID to Update: ");
                        int updateId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter New Name: ");
                        String newName = sc.nextLine();

                        String updateSQL = "UPDATE employees SET name = ? WHERE id = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
                        updateStmt.setString(1, newName);
                        updateStmt.setInt(2, updateId);
                        int updated = updateStmt.executeUpdate();
                        if (updated > 0) {
                            System.out.println("✅ Name updated.");
                        } else {
                            System.out.println("⚠️ Employee not found.");
                        }
                        updateStmt.close();
                        break;

                    case 4:
                        System.out.print("Enter Employee ID to Delete: ");
                        int deleteId = sc.nextInt();
                        String deleteSQL = "DELETE FROM employees WHERE id = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
                        deleteStmt.setInt(1, deleteId);
                        int deleted = deleteStmt.executeUpdate();
                        if (deleted > 0) {
                            System.out.println("🗑️ Employee deleted.");
                        } else {
                            System.out.println("⚠️ Employee not found.");
                        }
                        deleteStmt.close();
                        break;

                    case 5:
                        System.out.println("👋 Exiting program. Bye!");
                        conn.close();
                        sc.close();
                        return;

                    default:
                        System.out.println("❌ Invalid choice. Try again.");
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Error: ID already exists.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
