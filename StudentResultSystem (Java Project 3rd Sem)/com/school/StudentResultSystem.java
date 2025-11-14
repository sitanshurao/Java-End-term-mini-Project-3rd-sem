package com.school;

import com.school.models.*;
import com.school.processor.*;
import java.util.Scanner;

public class StudentResultSystem {
    private static ResultProcessor processor;
    private static Scanner scanner;
    private static User currentUser;
    
    public static void main(String[] args) {
        processor = new ResultProcessor();
        scanner = new Scanner(System.in);
        
        // Login/Signup System
        if (!welcomeScreen()) {
            System.out.println("Exiting system...");
            scanner.close();
            return;
        }
        
        System.out.println("\n✓ Login Successful! Welcome " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        
        boolean running = true;
        while (running) {
            displayMenu();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1: addStudentMenu(); break;
                    case 2: addSubjectMenu(); break;
                    case 3: processor.displayAllStudents(); break;
                    case 4: processor.displayAllSubjects(); break;
                    case 5: calculateStudentGrade(); break;
                    case 6: updateStudentMenu(); break;
                    case 7: deleteStudentMenu(); break;
                    case 8: 
                        LoginHistory.displayLoginHistory(currentUser.getUsername());
                        break;
                    case 9: 
                        if (currentUser.getRole().equals("ADMIN")) {
                            userManagementMenu();
                        } else {
                            System.out.println("Access Denied! Only ADMIN can manage users.");
                        }
                        break;
                    case 10: 
                        processor.saveData();
                        System.out.println("Data saved. Logging out...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static boolean welcomeScreen() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║       STUDENT RESULT SYSTEM                ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println("1. Login");
            System.out.println("2. Sign Up (New User)");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        if (login()) {
                            return true;
                        }
                        break;
                    case 2:
                        signUp();
                        break;
                    case 3:
                        return false;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
    
    private static boolean login() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║              LOGIN TO SYSTEM               ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("\nUsername: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            currentUser = processor.authenticateUser(username, password);
            
            if (currentUser != null) {
                return true;
            }
            
            attempts--;
            if (attempts > 0) {
                System.out.println("\n✗ Invalid credentials! Attempts remaining: " + attempts);
            } else {
                System.out.println("\n✗ Maximum login attempts exceeded!");
            }
        }
        
        return false;
    }
    
    private static void signUp() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           NEW USER REGISTRATION            ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine();
        
        // Check if username already exists
        if (processor.userExists(username)) {
            System.out.println("\n✗ Username already exists! Please choose a different username.");
            return;
        }
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("\n✗ Passwords do not match! Registration failed.");
            return;
        }
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Role (ADMIN/TEACHER): ");
        String role = scanner.nextLine().toUpperCase();
        
        if (!role.equals("ADMIN") && !role.equals("TEACHER")) {
            System.out.println("\n✗ Invalid role! Must be ADMIN or TEACHER.");
            return;
        }
        
        User newUser = new User(username, password, role, email);
        processor.addUser(newUser);
        
        System.out.println("\n✓ Registration Successful!");
        System.out.println("Your login history file has been created.");
        System.out.println("You can now login with your credentials.");
    }
    
    private static void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  STUDENT RESULT PROCESSING SYSTEM          ║");
        System.out.println("║  Logged in as: " + String.format("%-28s", currentUser.getUsername() + " [" + currentUser.getRole() + "]") + "║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("1. Add New Student");
        System.out.println("2. Add New Subject");
        System.out.println("3. Display All Students");
        System.out.println("4. Display All Subjects");
        System.out.println("5. Calculate Student Grade");
        System.out.println("6. Update Student Information");
        System.out.println("7. Delete Student");
        System.out.println("8. View My Login History");
        
        if (currentUser.getRole().equals("ADMIN")) {
            System.out.println("9. User Management (Admin Only)");
        }
        
        System.out.println("10. Save and Logout");
        System.out.print("\nEnter your choice: ");
    }
    
    private static void userManagementMenu() {
        System.out.println("\n--- USER MANAGEMENT ---");
        System.out.println("1. Add New User");
        System.out.println("2. Display All Users");
        System.out.println("3. View User Login History");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1: addUserMenu(); break;
                case 2: processor.displayAllUsers(); break;
                case 3: viewUserHistoryMenu(); break;
                case 4: return;
                default: System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
    
    private static void viewUserHistoryMenu() {
        System.out.print("\nEnter username to view history: ");
        String username = scanner.nextLine();
        LoginHistory.displayLoginHistory(username);
    }
    
    private static void addUserMenu() {
        System.out.println("\n--- ADD NEW USER (BY ADMIN) ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        
        if (processor.userExists(username)) {
            System.out.println("\n✗ Username already exists!");
            return;
        }
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Role (ADMIN/TEACHER): ");
        String role = scanner.nextLine().toUpperCase();
        
        if (!role.equals("ADMIN") && !role.equals("TEACHER")) {
            System.out.println("Invalid role! Must be ADMIN or TEACHER.");
            return;
        }
        
        User newUser = new User(username, password, role, email);
        processor.addUser(newUser);
    }
    
    private static void addStudentMenu() {
        try {
            System.out.println("\n--- ADD NEW STUDENT ---");
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Is this a graduating student? (yes/no): ");
            String isGraduating = scanner.nextLine();
            
            if (isGraduating.equalsIgnoreCase("yes")) {
                System.out.print("Enter Transcript Details: ");
                String transcript = scanner.nextLine();
                GraduatingStudent student = new GraduatingStudent(id, name, age, transcript);
                processor.addStudent(student);
            } else {
                Student student = new Student(id, name, age);
                processor.addStudent(student);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void addSubjectMenu() {
        try {
            System.out.println("\n--- ADD NEW SUBJECT ---");
            System.out.print("Enter Subject Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter Maximum Marks: ");
            double maxMarks = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Enter Obtained Marks: ");
            double obtainedMarks = Double.parseDouble(scanner.nextLine());
            
            Subject subject = new Subject(name, maxMarks, obtainedMarks);
            processor.addSubject(subject);
        } catch (NumberFormatException e) {
            System.out.println("Invalid marks format!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void calculateStudentGrade() {
        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = processor.findStudentById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        if (processor.getSubjects().isEmpty()) {
            System.out.println("No subjects available!");
            return;
        }
        
        Subject[] subjectArray = processor.getSubjects().toArray(new Subject[0]);
        
        String grade = processor.calculateGrade(student, subjectArray);
        boolean passed = processor.checkPassingEligibility(subjectArray);
        boolean promoted = processor.checkPromotionEligibility(subjectArray);
        
        System.out.println("\n=== RESULT SUMMARY ===");
        System.out.println(student.getSummary());
        System.out.println("\nSubjects:");
        for (Subject s : subjectArray) {
            System.out.println("  " + s);
        }
        System.out.println("\nOverall Grade: " + grade);
        System.out.println("Passing Status: " + (passed ? "PASSED" : "FAILED"));
        System.out.println("Promotion Status: " + (promoted ? "PROMOTED" : "NOT PROMOTED"));
        
        if (student instanceof GraduatingStudent) {
            ((GraduatingStudent) student).setEligible(promoted);
        }
    }
    
    private static void updateStudentMenu() {
        System.out.print("\nEnter Student ID to update: ");
        String id = scanner.nextLine();
        
        Student student = processor.findStudentById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Current Details: " + student.getSummary());
        System.out.print("\nEnter new name (or press Enter to skip): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            student.setName(name);
        }
        
        System.out.print("Enter new age (or press Enter to skip): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr);
                student.setAge(age);
            } catch (NumberFormatException e) {
                System.out.println("Invalid age format!");
            }
        }
        
        System.out.println("Student updated successfully!");
    }
    
    private static void deleteStudentMenu() {
        System.out.print("\nEnter Student ID to delete: ");
        String id = scanner.nextLine();
        processor.deleteStudent(id);
    }
}