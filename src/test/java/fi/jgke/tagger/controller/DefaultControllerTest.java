package fi.jgke.tagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.repository.TypeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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
@Transactional
public class DefaultControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private TagRepository tagRepository;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private List<Long> tags;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mapper = new ObjectMapper();

        this.tagRepository.deleteAll();
        tags = new ArrayList<>();
        tags.add(addTag("cat"));
        tags.add(addTag("cute"));
    }

    @Test
    public void tearDown() throws Exception {
        tags.forEach((t) -> tagRepository.delete(t));
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

    private Long addTag(String value) {
        Tag tag = new Tag();
        tag.setValue(value);
        return tagRepository.save(tag).getId();
    }

    @Test
    public void responseContainsAddedItem() throws Exception {
        class SourceParameter {

            public String title;
            public String url;
            public String sourcetype;
        }
        SourceParameter sourceParameter = new SourceParameter();
        String title = UUID.randomUUID().toString();
        String url = "https://placekitten.com/200/200?image=3";
        Type type = this.typeRepository.findAll().stream().findAny().get();

        sourceParameter.title = title;
        sourceParameter.url = url;
        sourceParameter.sourcetype = "types/" + type.getId();

        String sourcestring = mapper.writeValueAsString(sourceParameter);
        System.out.println("Sourcestring: " + sourcestring);

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
