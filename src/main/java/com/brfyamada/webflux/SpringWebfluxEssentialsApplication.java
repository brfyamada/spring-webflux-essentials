package com.brfyamada.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import reactor.blockhound.BlockHound;

//@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
@SpringBootApplication()
public class SpringWebfluxEssentialsApplication {
/*

	static {
		BlockHound.install();
	}
*/

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxEssentialsApplication.class, args);
	}

}
