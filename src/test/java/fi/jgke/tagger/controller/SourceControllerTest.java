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
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.io.IOException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SourceControllerTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void indexWorks() throws Exception {
        try (WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:" + port);
            Assert.assertEquals("Tagger", page.getTitleText());
        }
    }

    private HtmlPage addSource(HtmlPage page, String title, String url) throws IOException {
        HtmlForm form = page.getFormByName("addform");
        form.getInputByName("title").setValueAttribute(title);
        form.getInputByName("url").setValueAttribute(url);
        HtmlSelect sourcetypeSelect = form.getSelectByName("sourcetype");
        sourcetypeSelect.setSelectedAttribute(sourcetypeSelect.getOptions()
                .stream()
                .filter((t) -> t.asText().equals("html"))
                .findAny().get(), true);

        return form.getInputByName("addbutton").click();
    }

    @Test
    public void canAddASource() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String title = UUID.randomUUID().toString();
            String url = "http://example.com";

            HtmlPage page = webClient.getPage("http://localhost:" + port);
            page = addSource(page, title, url);

            Assert.assertTrue(page.asText().contains(title));
            Assert.assertTrue(page.asXml().contains(url));

            /* If the test fails before this, the source is left in the database... */
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

            HtmlPage page = webClient.getPage("http://localhost:" + port);
            page = addSource(page, title, url);
            page = addTag(page, tag);

            Assert.assertTrue(page.asText().contains(tag));

            page.getFormByName("deleteform").getInputByName("deletebutton").click();
        }
    }

    private HtmlPage searchTag(HtmlPage page, String tag) throws IOException {
        HtmlForm form = page.getFormByName("tagsearchform");
        form.getInputByName("tagstring").setValueAttribute(tag);
        return form.getInputByName("searchbutton").click();
    }

    @Test
    public void canSearchForTags() throws Exception {
        try (WebClient webClient = new WebClient()) {
            String title = UUID.randomUUID().toString();
            String nottitle = UUID.randomUUID().toString();
            String url = "http://example.com";
            String tag = UUID.randomUUID().toString();

            HtmlPage page = webClient.getPage("http://localhost:" + port);
            HtmlPage page2 = addSource(page, title, url);
            addSource(page, nottitle, url);
            addTag(page2, tag);
            page = searchTag(page, tag);
            Assert.assertTrue(page.asText().contains(title));
            Assert.assertFalse(page.asText().contains(nottitle));
        }
    }

}
