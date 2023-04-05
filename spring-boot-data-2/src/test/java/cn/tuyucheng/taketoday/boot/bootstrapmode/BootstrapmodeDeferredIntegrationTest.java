package cn.tuyucheng.taketoday.boot.bootstrapmode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.config.BootstrapMode;

import cn.tuyucheng.taketoday.boot.bootstrapmode.domain.Todo;
import cn.tuyucheng.taketoday.boot.bootstrapmode.repository.TodoRepository;

@DataJpaTest(bootstrapMode = BootstrapMode.DEFERRED)
class BootstrapmodeDeferredIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void givenBootstrapmodeValueIsDeferred_whenCreatingTodo_shouldSuccess() {
        Todo todo = new Todo("Something to be done");

        assertThat(todoRepository.save(todo)).hasNoNullFieldsOrProperties();
    }
}
