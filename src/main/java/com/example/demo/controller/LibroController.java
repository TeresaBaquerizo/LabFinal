package com.example.demo.controller;

import com.example.demo.models.Libro;
import com.example.demo.services.LibroService;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {

	@Autowired
	private LibroService libroService;

	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("libros", libroService.listarLibros());
		return "listar-libros";
	}

	@GetMapping("/crear-libro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("libro", new Libro());
		return "registro-libro";
	}

	@PostMapping("/guardar-libro")
	public String guardarLibro(@ModelAttribute Libro nuevoLibro, RedirectAttributes redirectAttributes) {
		boolean resultado = libroService.guardarLibro(nuevoLibro);
		if (resultado) {
			redirectAttributes.addFlashAttribute("mensaje", "Se guardó correctamente");
		} else {
			redirectAttributes.addFlashAttribute("mensaje", "Ocurrio un error, contactar con soporte");
		}
		return "redirect:/libros";
	}

	@GetMapping("/editar-libro/{id}")
	public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
		model.addAttribute("libro", libroService.listarLibroPorId(id));
		return "editar-libro";
	}

	@GetMapping("/eliminar-libro/{id}")
	public String eliminarLibro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		libroService.eliminarLibroPorId(id);
		redirectAttributes.addFlashAttribute("mensaje", "Se eliminó correctamente");
		return "redirect:/libros";
	}

	@GetMapping("/export-pdf")
	public ResponseEntity<byte[]> exportPdf() throws JRException, FileNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("report", "report.pdf");
		return ResponseEntity.ok().headers(headers).body(libroService.exportPdf());
	}

}
