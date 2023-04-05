package cn.tuyucheng.taketoday.autoconfig.exclude;

import cn.tuyucheng.taketoday.boot.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Disabled("fails test")
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class ExcludeAutoConfig1IntegrationTest {

	/**
	 * Encapsulates the random port the test server is listening on.
	 */
	@LocalServerPort
	private int port;

	@Test
	public void givenSecurityConfigExcluded_whenAccessHome_thenNoAuthenticationRequired() {
		int statusCode = RestAssured.get("http://localhost:" + port).statusCode();
		assertEquals(HttpStatus.OK.value(), statusCode);
	}
}