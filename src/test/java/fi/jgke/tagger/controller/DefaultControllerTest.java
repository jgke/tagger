package fi.jgke.tagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.domain.User;
import fi.jgke.tagger.repository.TypeRepository;
import fi.jgke.tagger.repository.UserRepository;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class DefaultControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRepository typeRepository;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private User user;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        this.mapper = new ObjectMapper();
    }
    
    @After
    public void tearDown() {
        this.userRepository.delete(this.user);
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void responseTypeApplicationHalJson() throws Exception {
        mockMvc.perform(get("/api/v1/sources"))
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"));
    }

    @Test
    public void responseContainsAddedItem() throws Exception {
        String title = UUID.randomUUID().toString();
        String url = UUID.randomUUID().toString();
        Type type = this.typeRepository.findByValue("html");
        User user = this.userRepository.findAll().stream().findAny().get();

        String sourcestring = "{\"title\": \"" + title + "\", \"url\": \"" + url + "\", "
                + "\"sourcetype\": " +  type.getId() + ", \"submitter\": \"" + user.getId() + "\"}";

        System.out.println("\n\n\nSourcestring: " + sourcestring + "\n\n\n");
        
        MvcResult postResponse = mockMvc.perform(
                post("/api/v1/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourcestring))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(
                get(postResponse.getResponse().getHeader("Location"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourcestring))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.url").value(url));

    }
}
