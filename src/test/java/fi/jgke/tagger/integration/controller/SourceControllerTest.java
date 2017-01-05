/*
 * Copyright 2016 Jaakko Hannikainen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package fi.jgke.tagger.integration.controller;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.repository.PersonRepository;
import fi.jgke.tagger.repository.SourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
public class SourceControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    PersonRepository personRepository;

    public final static String USERNAME = "test_user";
    public final static String PASSWORD = "test_password";

    @Before
    public void init() {
        if (personRepository.findByUsername(USERNAME) != null) {
            return;
        }
        Person person = new Person();
        person.setUsername(USERNAME);
        person.setPassword(PASSWORD);

        personRepository.save(person);
    }

    @Test
    public void indexWorks() throws Exception {
        try (WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:" + port);
            Assert.assertEquals("Tagger", page.getTitleText());
        }
    }

    private HtmlPage login(HtmlPage page) throws IOException {
        HtmlForm form = page.getFormByName("loginform");
        form.getInputByName("username").setValueAttribute(USERNAME);
        form.getInputByName("password").setValueAttribute(PASSWORD);
        return form.getButtonByName("loginbutton").click();
    }

    private HtmlPage authenticatedIndex(WebClient webClient) throws IOException {
        HtmlPage page = webClient.getPage("http://localhost:" + port + "/login");
        page = login(page);
        return page;
    }

    private HtmlPage addSource(HtmlPage page, String title, String url) throws IOException {
        HtmlForm form = page.getFormByName("addform");
        form.getInputByName("title").setValueAttribute(title);
        form.getInputByName("url").setValueAttribute(url);
        HtmlSelect sourcetypeSelect = form.getSelectByName("sourcetype");
        sourcetypeSelect.setSelectedAttribute(sourcetypeSelect.getOptions()
                .stream()
                .filter((t) -> t.asText().equals("image"))
                .findAny().get(), true);

        return form.getInputByName("addbutton").click();
    }

    @Test
    public void canAddASourceWithBadThumbnail() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String title = UUID.randomUUID().toString();
            String url = "http://example.com";

            HtmlPage page = authenticatedIndex(webClient);
            page = addSource(page, title, url);

            Assert.assertTrue(page.asText().contains(title));
            Assert.assertTrue(page.asXml().contains(url));

            String[] split = page.getUrl().getPath().split("/");
            Long id = Long.parseLong(split[split.length - 1]);
            String thumbnailUrl = "http://localhost:" + port + "/sources/" + id + "/thumbnail";
            String thumbnail = ((TextPage) webClient.getPage(thumbnailUrl)).getContent();
            System.out.println(thumbnailUrl);

            Assert.assertTrue(thumbnail.isEmpty());

            page.getFormByName("deleteform").getInputByName("deletebutton").click();
        }
    }

    @Test
    public void canAddASourceWithGoodThumbnail() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String title = UUID.randomUUID().toString();
            String url = "http://placekitten.com/200/400";

            HtmlPage page = authenticatedIndex(webClient);
            page = addSource(page, title, url);

            Assert.assertTrue(page.asText().contains(title));
            Assert.assertTrue(page.asXml().contains(url));

            String[] split = page.getUrl().getPath().split("/");
            Long id = Long.parseLong(split[split.length - 1]);
            String thumbnailUrl = "http://localhost:" + port + "/sources/" + id + "/thumbnail";
            String thumbnail = ((HtmlPage) webClient.getPage(thumbnailUrl)).asText();
            System.out.println(thumbnailUrl);
            Assert.assertTrue(!thumbnail.isEmpty());


            page.getFormByName("deleteform").getInputByName("deletebutton").click();
        }
    }

    private HtmlPage addTag(HtmlPage page, String tag) throws IOException {
        HtmlForm form = page.getFormByName("tagform");
        form.getInputByName("tagname").setValueAttribute(tag);
        return form.getInputByName("addbutton").click();
    }

    @Test
    public void canAddTagsToASource() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String title = UUID.randomUUID().toString();
            String url = "http://example.com";
            String tag = UUID.randomUUID().toString();
            tag = tag.substring(0, 32);

            HtmlPage page = authenticatedIndex(webClient);
            page = addSource(page, title, url);
            page = addTag(page, tag);

            Assert.assertTrue(page.asText().contains(tag));

            page.getFormByName("deleteform").getInputByName("deletebutton").click();
        }
    }

    private HtmlPage searchTag(HtmlPage page, String tag) throws IOException {
        HtmlForm form = page.getFormByName("tagsearchform");
        form.getInputByName("tagstring").setValueAttribute(tag);
        return page.getElementById("searchbutton").click();
    }

    @Test
    public void canSearchForTags() throws Exception {
        try (WebClient webClient = new WebClient()) {
            // Font Awesome's javascript loader breaks tests...?
            webClient.getOptions().setJavaScriptEnabled(false);
            String title = UUID.randomUUID().toString();
            String nottitle = UUID.randomUUID().toString();
            String url = "http://example.com";
            String tag = UUID.randomUUID().toString();
            tag = tag.substring(0, 32);

            HtmlPage page = authenticatedIndex(webClient);
            HtmlPage page2 = addSource(page, title, url);
            addSource(page, nottitle, url);
            addTag(page2, tag);
            page = searchTag(page, tag);
            Assert.assertTrue(page.asText().contains(title));
            Assert.assertFalse(page.asText().contains(nottitle));
            page = page.getAnchorByText(title).click();
            page.getFormByName("deleteform").getInputByName("deletebutton").click();
        }
    }

    private HtmlPage addComment(HtmlPage page, String comment) throws IOException {
        HtmlTextArea textInput = (HtmlTextArea) page.getElementById("commentfield");
        textInput.setText(comment);
        return page.getElementById("commentbutton").click();
    }

    @Test
    public void canAddAComment() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String title = UUID.randomUUID().toString();
            String url = "http://example.com";

            HtmlPage page = authenticatedIndex(webClient);
            page = addSource(page, title, url);

            Assert.assertTrue(page.asText().contains(title));
            Assert.assertTrue(page.asXml().contains(url));

            String comment = "This is a comment.";
            page = addComment(page, comment);

            Assert.assertTrue(page.asText().contains(comment));

            /* If the test fails before this, the source is left in the database... */
            page.getFormByName("deleteform").getInputByName("deletebutton").click();
        }
    }

    @Test(expected = FailingHttpStatusCodeException.class)
    public void getErrorWhenRequestingUnknownSource() throws Exception {
        try (WebClient webClient = new WebClient()) {
            webClient.getPage("http://localhost:" + port + "/sources/0/");
        }
    }
}
