package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity(name = "Jury")
@DiscriminatorValue("JURY")
public class Jury extends Voter {

    private String jobTitle;

    public Jury() {
        super();
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}