package com.example.demo.services;

import com.example.demo.models.Libro;
import com.example.demo.models.LibroRequest;
import com.example.demo.repository.LibroRepository;
import com.example.demo.util.ReportGenerator;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private ReportGenerator reportGenerator;

	public boolean guardarLibro(Libro libro) {
		boolean resultado;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String fechaFormateada = libro.getFechaPublicacion().format(formatter);

			libro.setFechaPublicacion(LocalDate.parse(fechaFormateada, formatter));
			libro.setFechaRegistro(LocalDate.now());
			Libro libroGuardado = libroRepository.save(libro);

			if (libroGuardado != null) {
				System.out.println("El id del libro guardado es: " + libroGuardado.getId());
				resultado = true;
			} else {
				System.out.println("Ocurrió un error al guardar el libro");
				resultado = false;
			}
		} catch (Exception e) {
			System.out.println("Error al formatear o guardar la fecha: " + e.getMessage());
			resultado = false;
		}

		return resultado;
	}

	public List<LibroRequest> listarLibros() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<Libro> libros = libroRepository.findAll();

		return libros.stream().map(libro -> {
			LibroRequest libroRequest = new LibroRequest();
			libroRequest.setId(libro.getId());
			libroRequest.setNombreLibro(libro.getNombreLibro());
			libroRequest.setNombreAutor(libro.getNombreAutor());
			libroRequest.setGenero(libro.getGenero());
			libroRequest.setFechaPublicacion(libro.getFechaPublicacion().format(formatter));
			return libroRequest;
		}).collect(Collectors.toList());
	}

	public LibroRequest listarLibroPorId(Long id) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Libro libro = libroRepository.findById(id).get();
		LibroRequest libroRequest = new LibroRequest();
		libroRequest.setId(libro.getId());
		libroRequest.setNombreLibro(libro.getNombreLibro());
		libroRequest.setNombreAutor(libro.getNombreAutor());
		libroRequest.setGenero(libro.getGenero());
		libroRequest.setFechaPublicacion(libro.getFechaPublicacion().format(formatter));

		return libroRequest;
	}

	public List<LibroRequest> listarLibrosParaRepote() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
		List<Libro> libros = libroRepository.findAllByFechaRegistroLastSixMonths(sixMonthsAgo);
		
		return libros.stream().map(libro -> {
			LibroRequest libroRequest = new LibroRequest();
			libroRequest.setId(libro.getId());
			libroRequest.setNombreLibro(libro.getNombreLibro());
			libroRequest.setNombreAutor(libro.getNombreAutor());
			libroRequest.setGenero(libro.getGenero());
			libroRequest.setFechaPublicacion(libro.getFechaPublicacion().format(formatter));
			return libroRequest;
		}).collect(Collectors.toList());
	}

	public void eliminarLibroPorId(Long id) {
		libroRepository.deleteById(id);
	}

	public byte[] exportPdf() throws JRException, FileNotFoundException {
		return reportGenerator.exportToPdf(this.listarLibrosParaRepote());
	}
}