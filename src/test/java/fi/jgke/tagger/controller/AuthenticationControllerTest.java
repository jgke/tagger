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
package fi.jgke.tagger.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.repository.PersonRepository;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
public class AuthenticationControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    PersonRepository personRepository;

    private HtmlPage register(HtmlPage page, String username, String password) throws IOException {
        page = page.getElementById("registerlink").click();
        HtmlForm form = page.getFormByName("registerform");
        form.getInputByName("username").setValueAttribute(username);
        form.getInputByName("password").setValueAttribute(password);
        return form.getButtonByName("registerbutton").click();
    }

    private HtmlPage login(HtmlPage page, String username, String password) throws IOException {
        page = page.getElementById("loginlink").click();
        HtmlForm form = page.getFormByName("loginform");
        form.getInputByName("username").setValueAttribute(username);
        form.getInputByName("password").setValueAttribute(password);
        return form.getButtonByName("loginbutton").click();
    }

    private HtmlPage unauthenticatedIndex(WebClient webClient, String path) throws IOException {
        HtmlPage page = webClient.getPage("http://localhost:" + port + "/" + path);
        return page;
    }

    private HtmlPage addSource(HtmlPage page, String title, String url) throws IOException {
        page = page.getElementById("frontpagelink").click();
        HtmlForm form = page.getFormByName("addform");
        form.getInputByName("title").setValueAttribute(title);
        form.getInputByName("url").setValueAttribute(url);
        HtmlSelect sourcetypeSelect = form.getSelectByName("sourcetype");
        sourcetypeSelect.setSelectedAttribute(sourcetypeSelect.getOptions().stream()
                .filter((t) -> t.asText().equals("link"))
                .findAny().get(), true);

        return form.getInputByName("addbutton").click();
    }

    @Test
    public void canRegister() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String username = UUID.randomUUID().toString();
            String password = UUID.randomUUID().toString();

            HtmlPage page = unauthenticatedIndex(webClient, "register");
            page = register(page, username, password);
            page = login(page, username, password);
            Assert.assertTrue(page.asText().contains(username));
        }
    }

    @Test
    public void canDeleteAddedSource() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String username = UUID.randomUUID().toString();
            String password = UUID.randomUUID().toString();
            String title = UUID.randomUUID().toString();
            String url = "http://example.com";

            HtmlPage page = unauthenticatedIndex(webClient, "register");
            page = register(page, username, password);
            page = login(page, username, password);
            page = addSource(page, title, url);

            Assert.assertTrue(page.asText().contains(title));
            Assert.assertTrue(page.asXml().contains(url));

            page = page.getFormByName("deleteform").getInputByName("deletebutton").click();

            Assert.assertFalse(page.asText().contains(title));
        }
    }

}
