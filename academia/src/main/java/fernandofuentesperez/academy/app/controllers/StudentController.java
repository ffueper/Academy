package fernandofuentesperez.academy.app.controllers;

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
import org.springframework.security.access.prepost.PreAuthorize;
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

import fernandofuentesperez.academy.app.models.entities.Student;
import fernandofuentesperez.academy.app.models.services.ClientService;
import fernandofuentesperez.academy.app.models.services.StudentService;
import fernandofuentesperez.academy.app.models.services.UploadFileService;
import fernandofuentesperez.academy.app.util.paginator.PageRender;

@Controller
@SessionAttributes("student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ClientService clientService;

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
	

	@GetMapping(value = "/studentProfile/{id}")
	public String seeStudent(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Student student = studentService.findOne(id);
		if (student == null) {
			flash.addFlashAttribute("error", "El alumno no existe en la base de datos");
			return "redirect:/studentList";
		}

		model.put("student", student);
		model.put("titulo", "Alumno:  " + student.getName() + " " + student.getSurname());
		return "studentProfile";
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/studentList", method = RequestMethod.GET)
	public String listStudent(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4); // Spring Boot 2

		Page<Student> students = studentService.findAll(pageRequest);

		PageRender<Student> pageRender = new PageRender<Student>("/studentList", students);
		model.addAttribute("titulo", "Listado de alumnos");
		model.addAttribute("students", students);
		model.addAttribute("page", pageRender);
		return "studentList";

	}

	
	@RequestMapping(value = "/studentForm/{id}", method = RequestMethod.GET)
	public String createStudent(@PathVariable(value = "id") Long id, Map<String, Object> model) {

		Student student = new Student();

		student.setClient(clientService.findOne(id));
		student.setPhoto(""); // Campo photo no puede ser null.

		model.put("student", student);
		model.put("titulo", "Crear Alumno");

		return "studentForm";
	}

	
	@RequestMapping(value = "/studentForm", method = RequestMethod.POST)
	public String saveStudent(@Valid Student student, BindingResult result, Model model,
			@RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Alumno");
			return "studentForm";
		}

		if (!photo.isEmpty()) {

			if (student.getId() != null && student.getId() > 0 && student.getPhoto() != null
					&& student.getPhoto().length() > 0) {

				uploadFileService.delete(student.getPhoto());
			}

			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(photo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFileName + "'");

			student.setPhoto(uniqueFileName);
		}

		// Si crea un alumno con foto le meto cadena vacía para que no de error
		// alumno.setPhoto("");

		String msflash = (student.getId() != null) ? "Alumno editado con éxito." : "Alumno creado con éxito.";
		studentService.save(student);
		status.setComplete(); // Elimina el objeto alumno de la sesión
		flash.addFlashAttribute("success", msflash);
		return "redirect:clientProfile/" + student.getClient().getId();
	}

	
	@RequestMapping(value = "/studentForm/{id}")
	public String editStudent(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {
		Student student = null;

		if (id > 0) {
			student = studentService.findOne(id);
			if (student == null) {
				flash.addFlashAttribute("error", "El ID del alumno no existe en la BBDD!");
				return "redirect:/studentList";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del alumno no puede ser menor que 1");
			return "redirect:/studentList";
		}

		model.put("student", student);
		model.put("titulo", "Editar Alumno");

		return "studentForm";
	}

	
	@RequestMapping(value = "/deleteStudent/{id}")
	public String deleteStudent(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Student student = studentService.findOne(id);

			studentService.delete(id);
			flash.addFlashAttribute("success", "Alumno eliminado con éxito.");

			if (uploadFileService.delete(student.getPhoto())) {
				flash.addFlashAttribute("info", "Foto " + student.getPhoto() + " eliminada con éxito");
			}

		}
		return "redirect:/studentList";
	}

	
	@RequestMapping(value = "/deleteStudentOfClient/{idStudent}")
	public String deleteStudentOfClient(@PathVariable(value = "idStudent") Long idStudent, RedirectAttributes flash) {
		Student student = null;
		if (idStudent > 0) {
			student = studentService.findOne(idStudent);

			studentService.delete(idStudent);
			flash.addFlashAttribute("success", "Alumno eliminado con éxito.");

			if (uploadFileService.delete(student.getPhoto())) {
				flash.addFlashAttribute("info", "Foto " + student.getPhoto() + " eliminada con éxito");
			}
			return "redirect:/clientProfile/" + student.getClient().getId();

		}
		return "redirect:/clientList";
	}
	

	/*private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(role));
	}*/

}
