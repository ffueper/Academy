package fernandofuentesperez.academy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fernandofuentesperez.academy.app.models.services.UploadFileService;

@SpringBootApplication
public class AcademyApplication implements CommandLineRunner{

	@Autowired
	UploadFileService uploadFileService;
	
	public static void main(String[] args) {
		SpringApplication.run(AcademyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
		
	}

}
