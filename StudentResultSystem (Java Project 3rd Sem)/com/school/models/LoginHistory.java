package com.school.models;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginHistory {
    private static final String HISTORY_DIR = "login_history/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    public static void recordLogin(String username, boolean success) {
        try {
            // Create directory if it doesn't exist
            File dir = new File(HISTORY_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            
            String fileName = HISTORY_DIR + username + "_login_history.txt";
            FileWriter fw = new FileWriter(fileName, true); // append mode
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            
            String status = success ? "SUCCESS" : "FAILED";
            String timestamp = LocalDateTime.now().format(formatter);
            
            pw.println("===============================================");
            pw.println("Login Attempt: " + status);
            pw.println("Date & Time: " + timestamp);
            pw.println("Username: " + username);
            pw.println("===============================================");
            pw.println();
            
            pw.close();
            bw.close();
            fw.close();
            
        } catch (IOException e) {
            System.out.println("Error recording login history: " + e.getMessage());
        }
    }
    
    public static void displayLoginHistory(String username) {
        String fileName = HISTORY_DIR + username + "_login_history.txt";
        File file = new File(fileName);
        
        if (!file.exists()) {
            System.out.println("No login history found for user: " + username);
            return;
        }
        
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║     LOGIN HISTORY FOR: " + String.format("%-20s", username) + "║");
            System.out.println("╚════════════════════════════════════════════╝\n");
            
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            
            br.close();
            fr.close();
            
        } catch (IOException e) {
            System.out.println("Error reading login history: " + e.getMessage());
        }
    }
    
    public static void recordRegistration(String username, String email, String role) {
        try {
            File dir = new File(HISTORY_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            
            String fileName = HISTORY_DIR + username + "_login_history.txt";
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            
            String timestamp = LocalDateTime.now().format(formatter);
            
            pw.println("===============================================");
            pw.println("NEW USER REGISTRATION");
            pw.println("Date & Time: " + timestamp);
            pw.println("Username: " + username);
            pw.println("Email: " + email);
            pw.println("Role: " + role);
            pw.println("===============================================");
            pw.println();
            
            pw.close();
            bw.close();
            fw.close();
            
        } catch (IOException e) {
            System.out.println("Error recording registration: " + e.getMessage());
        }
    }
}