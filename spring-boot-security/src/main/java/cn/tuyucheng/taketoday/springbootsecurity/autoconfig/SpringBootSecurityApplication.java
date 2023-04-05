package cn.tuyucheng.taketoday.springbootsecurity.autoconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {
	SecurityAutoConfiguration.class
	//    ,ManagementWebSecurityAutoConfiguration.class
}, scanBasePackages = "cn.tuyucheng.taketoday.springbootsecurity.autoconfig")
public class SpringBootSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityApplication.class, args);
	}
}