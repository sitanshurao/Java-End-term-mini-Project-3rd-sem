package com.school.processor;

import com.school.models.*;
import java.io.*;
import java.util.ArrayList;

public class ResultProcessor {
    private ArrayList<Student> students;
    private ArrayList<Subject> subjects;
    private ArrayList<User> users;
    private static final String STUDENT_FILE = "students.dat";
    private static final String SUBJECT_FILE = "subjects.dat";
    private static final String USER_FILE = "users.dat";
    
    public ResultProcessor() {
        students = new ArrayList<>();
        subjects = new ArrayList<>();
        users = new ArrayList<>();
        loadData();
        initializeDefaultUsers();
    }
    
    private void initializeDefaultUsers() {
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", "ADMIN", "admin@school.com"));
            users.add(new User("teacher", "teacher123", "TEACHER", "teacher@school.com"));
            saveUserData();
            System.out.println("Default users created:");
            System.out.println("  Username: admin | Password: admin123 | Role: ADMIN");
            System.out.println("  Username: teacher | Password: teacher123 | Role: TEACHER");
            
            // Record initial registration in history
            LoginHistory.recordRegistration("admin", "admin@school.com", "ADMIN");
            LoginHistory.recordRegistration("teacher", "teacher@school.com", "TEACHER");
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            FileInputStream fis = new FileInputStream(STUDENT_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            students = (ArrayList<Student>) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Student data loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No existing student data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
        
        try {
            FileInputStream fis = new FileInputStream(SUBJECT_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            subjects = (ArrayList<Subject>) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Subject data loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No existing subject data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading subject data: " + e.getMessage());
        }
        
        try {
            FileInputStream fis = new FileInputStream(USER_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (ArrayList<User>) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("User data loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No existing user data found. Creating defaults.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }
    
    public void saveData() {
        try {
            FileOutputStream fos = new FileOutputStream(STUDENT_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(students);
            oos.close();
            fos.close();
            System.out.println("Student data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
        
        try {
            FileOutputStream fos = new FileOutputStream(SUBJECT_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(subjects);
            oos.close();
            fos.close();
            System.out.println("Subject data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving subject data: " + e.getMessage());
        }
        
        saveUserData();
    }
    
    private void saveUserData() {
        try {
            FileOutputStream fos = new FileOutputStream(USER_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
    
    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.authenticate(username, password)) {
                LoginHistory.recordLogin(username, true);
                return user;
            }
        }
        LoginHistory.recordLogin(username, false);
        return null;
    }
    
    public boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public void addUser(User user) {
        users.add(user);
        saveUserData();
        LoginHistory.recordRegistration(user.getUsername(), user.getEmail(), user.getRole());
        System.out.println("User registered successfully!");
    }
    
    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users found!");
            return;
        }
        
        System.out.println("\n=== ALL USERS ===");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
    }
    
    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Student added successfully!");
    }
    
    public void addSubject(Subject subject) {
        subjects.add(subject);
        System.out.println("Subject added successfully!");
    }
    
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found!");
            return;
        }
        
        System.out.println("\n=== ALL STUDENTS ===");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("\n" + (i + 1) + ". " + students.get(i).getSummary());
        }
    }
    
    public void displayAllSubjects() {
        if (subjects.isEmpty()) {
            System.out.println("No subjects found!");
            return;
        }
        
        System.out.println("\n=== ALL SUBJECTS ===");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }
    }
    
    public Student findStudentById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
    
    public boolean deleteStudent(String id) {
        Student student = findStudentById(id);
        if (student != null) {
            students.remove(student);
            System.out.println("Student deleted successfully!");
            return true;
        }
        System.out.println("Student not found!");
        return false;
    }
    
    public String calculateGrade(Student student, Subject[] subjects) {
        double totalMarks = 0;
        double totalMaxMarks = 0;
        
        for (Subject subject : subjects) {
            totalMarks += subject.getObtainedMarks();
            totalMaxMarks += subject.getMaxMarks();
        }
        
        double percentage = (totalMarks / totalMaxMarks) * 100;
        
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B+";
        else if (percentage >= 60) return "B";
        else if (percentage >= 50) return "C";
        else if (percentage >= 40) return "D";
        else return "F";
    }
    
    public boolean checkPassingEligibility(Subject[] subjects) {
        for (Subject subject : subjects) {
            if (subject.getPercentage() < 40) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkPromotionEligibility(Subject[] subjects) {
        double totalPercentage = 0;
        for (Subject subject : subjects) {
            totalPercentage += subject.getPercentage();
        }
        double avgPercentage = totalPercentage / subjects.length;
        
        return checkPassingEligibility(subjects) && avgPercentage >= 50;
    }
    
    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Subject> getSubjects() { return subjects; }
}