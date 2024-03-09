package com.example.demo.repository;

import com.example.demo.models.Libro;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibroRepository extends JpaRepository<Libro, Long> {

	@Query("SELECT l FROM Libro l WHERE l.fechaRegistro >= :sixMonthsAgo")
	List<Libro> findAllByFechaRegistroLastSixMonths(LocalDate sixMonthsAgo);
}
