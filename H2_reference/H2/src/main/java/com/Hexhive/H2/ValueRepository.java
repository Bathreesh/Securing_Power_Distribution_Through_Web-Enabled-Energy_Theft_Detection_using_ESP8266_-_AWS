package com.Hexhive.H2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITORY INTERFACE (The Data Manager)
 * 
 * This interface handles all communication with the database.
 * By extending JpaRepository, we automatically get methods like:
 * .save() - To insert or update data
 * .findAll() - To fetch all rows
 * .findById() - To find a specific record
 */
@Repository
public interface ValueRepository extends JpaRepository<ValueEntity, Long> {
    // We don't need to write any SQL! Spring Data JPA builds it for us.
}