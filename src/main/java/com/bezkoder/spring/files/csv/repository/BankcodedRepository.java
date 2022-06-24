package com.bezkoder.spring.files.csv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.files.csv.model.Bankcoded;

public interface BankcodedRepository extends JpaRepository<Bankcoded, Long> {
    
}
