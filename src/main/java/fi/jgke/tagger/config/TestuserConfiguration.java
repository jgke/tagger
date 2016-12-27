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
package fi.jgke.tagger.config;

import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.repository.PersonRepository;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("testing")
public class TestuserConfiguration {
    @Autowired
    PersonRepository personRepository;

    public final static String USERNAME = "test_user";
    public final static String PASSWORD = "test_password";

    @PostConstruct
    public void init() {
        if(personRepository.findByUsername(USERNAME) != null)
            return;
        Person person = new Person();
        person.setUsername(USERNAME);
        person.setPassword(PASSWORD);
        person.setSources(new ArrayList<>());
        personRepository.save(person);
    }
}
