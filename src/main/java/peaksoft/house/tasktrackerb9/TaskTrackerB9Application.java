package peaksoft.house.tasktrackerb9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class TaskTrackerB9Application {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerB9Application.class, args);
	}
	@GetMapping
	public String greetings(){
		return "index";
	}

}
