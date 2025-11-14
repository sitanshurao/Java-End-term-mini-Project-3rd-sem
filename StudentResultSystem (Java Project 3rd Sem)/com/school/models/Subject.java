package com.school.models;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String subjectName;
    private double maxMarks;
    private double obtainedMarks;
    
    public Subject(String subjectName, double maxMarks, double obtainedMarks) throws Exception {
        this.subjectName = subjectName;
        this.maxMarks = maxMarks;
        if (obtainedMarks < 0 || obtainedMarks > maxMarks) {
            throw new InputMismatchException("Marks must be between 0 and " + maxMarks);
        }
        this.obtainedMarks = obtainedMarks;
    }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(double maxMarks) { this.maxMarks = maxMarks; }
    public double getObtainedMarks() { return obtainedMarks; }
    
    public void setObtainedMarks(double obtainedMarks) throws Exception {
        if (obtainedMarks < 0 || obtainedMarks > maxMarks) {
            throw new InputMismatchException("Marks must be between 0 and " + maxMarks);
        }
        this.obtainedMarks = obtainedMarks;
    }
    
    public double getPercentage() {
        return (obtainedMarks / maxMarks) * 100;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %.2f/%.2f (%.2f%%)", 
            subjectName, obtainedMarks, maxMarks, getPercentage());
    }
}