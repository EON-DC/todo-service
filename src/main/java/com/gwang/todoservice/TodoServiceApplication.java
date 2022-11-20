package com.gwang.todoservice;

import com.gwang.todoservice.model.Todo;
import com.gwang.todoservice.repository.TodoRepository;
import com.gwang.todoservice.service.JsonPlaceholderService;
import com.gwang.todoservice.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication

public class TodoServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(TodoServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TodoServiceApplication.class, args);
    }


    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner commandLineRunner(JsonPlaceholderService jsonPlaceholderService,
                                        TodoRepository repository,
										TodoService service) {

        return args -> {
            // when the application loads get the 200 todos from json placeholder
            List<Todo> todos = jsonPlaceholderService.getTodos();
            LOG.info("Saved {}  todos in the database", todos.size());

            // once we have th todos persist them to thd db
            repository.saveAll(todos);

            // push this information to our dashboard service
			service.sendToDashBoard(todos);

        };
    }
}
