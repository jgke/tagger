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
package fi.jgke.tagger.service;

import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person registerPerson(String username, String password) {
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        return personRepository.save(person);
    }

    public Person getAuthenticatedPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return personRepository.findByUsername(authentication.getName());
    }

    public boolean canCurrentUserModifySource(Source source) {
        boolean authenticated = !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
        Person person = null;

        if (!authenticated) {
            return false;
        }

        person = getAuthenticatedPerson();
        return person.equals(source.getPerson()) || person.isAdmin();
    }
}
