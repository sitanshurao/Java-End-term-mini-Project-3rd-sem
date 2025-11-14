package com.school.models;

public class GraduatingStudent extends Student {
    private static final long serialVersionUID = 1L;
    
    private String transcript;
    private boolean eligible;
    
    public GraduatingStudent(String id, String name, int age, String transcript) {
        super(id, name, age);
        this.transcript = transcript;
        this.eligible = false;
    }
    
    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }
    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }
    
    @Override
    public String getSummary() {
        return super.getSummary() + 
               String.format("\nTranscript: %s\nEligible for Graduation: %s", 
               transcript, eligible ? "Yes" : "No");
    }
}