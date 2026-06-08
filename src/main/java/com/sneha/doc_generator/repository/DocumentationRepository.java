package com.sneha.doc_generator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sneha.doc_generator.model.Documentation;

@Repository
public interface DocumentationRepository extends JpaRepository<Documentation, Long> {

}
