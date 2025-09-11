package com.user.CustomerProfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerProfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerProfileApplication.class, args);
	} 

}
