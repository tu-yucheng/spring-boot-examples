package cn.tuyucheng.taketoday.keycloak;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {KeycloakApplication.class})
public class KeycloakContextIntegrationTest {

	@Test
	public void whenLoadApplication_thenSuccess() {
	}
}