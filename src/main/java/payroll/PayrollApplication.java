package payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// this is a meta-annotation ? I guess it pulls in all the SpringBoot stuff
// this means component scaling, auto config, property support
// basically, it makes a server
@SpringBootApplication
public class PayrollApplication {

	// entry point for entire application?
	public static void main(String[] args) {
		SpringApplication.run(PayrollApplication.class, args);
	}

}
