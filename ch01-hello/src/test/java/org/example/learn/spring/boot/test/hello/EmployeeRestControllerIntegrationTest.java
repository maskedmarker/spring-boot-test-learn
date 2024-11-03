package org.example.learn.spring.boot.test.hello;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Testing With @SpringBootTest
 * As the name suggests, integration tests focus on integrating different layers of the application. That also means no mocking is involved.
 *
 * Ideally, we should keep the integration tests separated from the unit tests and should not run along with the unit tests.
 * We can do this by using a different profile to only run the integration tests.
 * A couple of reasons for doing this could be that the integration tests are time-consuming and might need an actual database to execute.
 * However in this article, we won’t focus on that, and we’ll instead make use of the in-memory H2 persistence storage.
 *
 * The integration tests need to start up a container to execute the test cases.
 * Hence, some additional setup is required for this — all of this is easy in Spring Boot:
 * The @SpringBootTest annotation is useful when we need to bootstrap the entire container. The annotation works by creating the ApplicationContext that will be utilized in our tests.
 * We can use the webEnvironment attribute of @SpringBootTest to configure our runtime environment; we’re using WebEnvironment.MOCK here so that the container will operate in a mock servlet environment.
 * Next, the @TestPropertySource annotation helps configure the locations of properties files specific to our tests. Note that the property file loaded with @TestPropertySource will override the existing application.properties file.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class EmployeeRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeRepository repository;

    @After
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateEmployee() throws IOException, Exception {
        Employee bob = new Employee("bob");
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(bob)));

        List<Employee> found = repository.findAll();
        assertThat(found).extracting(Employee::getName).containsOnly("bob");
    }

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
        createTestEmployee("bob");
        createTestEmployee("alex");

        // @formatter:off
        mvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", is("bob")))
                .andExpect(jsonPath("$[1].name", is("alex")));
        // @formatter:on
    }

    //

    private void createTestEmployee(String name) {
        Employee emp = new Employee(name);
        repository.saveAndFlush(emp);
    }
}
