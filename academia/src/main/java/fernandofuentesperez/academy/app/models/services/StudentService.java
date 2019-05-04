package fernandofuentesperez.academy.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fernandofuentesperez.academy.app.models.entities.Student;

public interface StudentService {

	public List<Student> findAll();
	
	public Page<Student> findAll(Pageable pageable);
	
	public void save(Student alumno);
	
	public Student findOne(Long id);
	
	public void delete(Long id);
}
