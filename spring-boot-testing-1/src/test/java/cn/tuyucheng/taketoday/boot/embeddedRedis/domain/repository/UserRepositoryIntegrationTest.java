package cn.tuyucheng.taketoday.boot.embeddedRedis.domain.repository;

import cn.tuyucheng.taketoday.boot.embeddedRedis.TestRedisConfiguration;
import cn.tuyucheng.taketoday.boot.embeddedRedis.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser_toRedis() {
        final UUID id = UUID.randomUUID();
        final User user = new User(id, "name");

        final User saved = userRepository.save(user);

        assertNotNull(saved);
    }
}