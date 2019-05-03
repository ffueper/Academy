package fernandofuentesperez.academia.app.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernandofuentesperez.academia.app.models.entities.Alumno;
import fernandofuentesperez.academia.app.models.services.AlumnoService;
import fernandofuentesperez.academia.app.models.services.UploadFileService;
import fernandofuentesperez.academia.app.util.paginator.PageRender;

@Controller
@SessionAttributes("alumno")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;
	
	@Autowired
	private UploadFileService uploadFileService;

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> seePhoto(@PathVariable String filename) {

		Resource resource = null;

		try {
			resource = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	// Envía los datos de un alumno al profile.html
	@GetMapping(value = "/profile/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Alumno alumno = alumnoService.findOne(id);
		if (alumno == null) {
			flash.addFlashAttribute("error", "El alumno no existe en la base de datos");
			return "redirect:/alumnos";
		}

		model.put("alumno", alumno);
		model.put("titulo", "Perfil del alumno: " + alumno.getName());
		return "profile";
	}

	// Envía un Page con 5 alumnos usando PageRequest
	@RequestMapping(value = "/alumnos", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4); // Spring Boot 2

		Page<Alumno> alumnos = alumnoService.findAll(pageRequest);

		PageRender<Alumno> pageRender = new PageRender<Alumno>("/alumnos", alumnos);
		model.addAttribute("titulo", "Listado de alumnos");
		model.addAttribute("alumnos", alumnos);
		model.addAttribute("page", pageRender);
		return "alumnos";

	}

	// Envía un alumno sin datos al formulario
	@RequestMapping(value = "/formAlumno", method = RequestMethod.GET)
	public String crear(Map<String, Object> model) {

		Alumno alumno = new Alumno();
		alumno.setPhoto(""); //Campo photo no puede ser null.

		model.put("alumno", alumno);
		model.put("titulo", "Crear Alumno");

		return "formAlumno";
	}

	// Guarda un alumno en el sistema
	@RequestMapping(value = "/formAlumno", method = RequestMethod.POST)
	public String guardar(@Valid Alumno alumno, BindingResult result, Model model,
			@RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Alumno");
			return "formAlumno";
		}

		if (!photo.isEmpty()) {

			if (alumno.getId() != null && alumno.getId() > 0 && alumno.getPhoto() != null
					&& alumno.getPhoto().length() > 0) {

				uploadFileService.delete(alumno.getPhoto());
			}

			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(photo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFileName + "'");

			alumno.setPhoto(uniqueFileName);
		}

		// Si crea un alumno con foto le meto cadena vacía para que no de error
		//alumno.setPhoto("");

		String msflash = (alumno.getId() != null) ? "Alumno editado con éxito." : "Alumno creado con éxito.";
		alumnoService.save(alumno);
		status.setComplete(); // Elimina el objeto alumno de la sesión
		flash.addFlashAttribute("success", msflash);
		return "redirect:alumnos";
	}

	// Busca los datos del alumno y los envía al formulario
	@RequestMapping(value = "/formAlumno/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Alumno alumno = null;

		if (id > 0) {
			alumno = alumnoService.findOne(id);
			if (alumno == null) {
				flash.addFlashAttribute("error", "El ID del alumno no existe en la BBDD!");
				return "redirect:/alumnos";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del alumno no puede ser menor que 1");
			return "redirect:/alumnos";
		}

		model.put("alumno", alumno);
		model.put("titulo", "Editar Alumno");

		return "formAlumno";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Alumno alumno = alumnoService.findOne(id);

			alumnoService.delete(id);
			flash.addFlashAttribute("success", "Alumno eliminado con éxito.");

			if (uploadFileService.delete(alumno.getPhoto())) {
				flash.addFlashAttribute("info", "Foto " + alumno.getPhoto() + " eliminada con éxito");
			}

		}
		return "redirect:/alumnos";
	}

}
