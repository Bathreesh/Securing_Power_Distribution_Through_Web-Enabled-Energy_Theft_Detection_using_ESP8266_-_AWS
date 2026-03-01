package com.Hexhive.H2;

import jakarta.persistence.*;

/**
 * DATABASE ENTITY (The Blueprint)
 * 
 * An "Entity" represents a table in your database.
 * Every instance of this class will be a row in the table.
 */
@Entity
@Table(name = "app_value") // Specifies the name of the table in the database
public class ValueEntity {

    @Id // Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Database will auto-increment this ID
    private Long id;

    // This field stores the actual number we see on the webpage
    private Integer currentValue = 1; // Default starting value

    /**
     * JPA (Java Persistence API) requires a no-argument constructor to work.
     */
    public ValueEntity() {
    }

    // --- GETTERS AND SETTERS ---
    // These allow other classes to safely read and write the private data.

    public Long getId() {
        return id;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }
}