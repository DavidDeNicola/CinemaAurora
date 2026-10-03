package org.elis.movieexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieExplorerApplication.class, args);
	}

}
