package org.collectiveone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class C1ApiApp { 
    public static Logger logger = LoggerFactory.getLogger(C1ApiApp.class);
	public static void main(String[] args) {
	    //Remove the logs if required, this just to demo
	    logger.info("Starting Application");
	    logger.debug("Starting Application");
	    logger.error("Starting Application");
	    logger.warn("Starting Application");
		SpringApplication.run(C1ApiApp.class, args);
	}

}
