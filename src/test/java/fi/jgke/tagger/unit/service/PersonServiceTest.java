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
package fi.jgke.tagger.unit.service;

import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.domain.Tag;
import fi.jgke.tagger.repository.PersonRepository;
import fi.jgke.tagger.repository.SourceRepository;
import fi.jgke.tagger.repository.TagRepository;
import fi.jgke.tagger.service.PersonService;
import fi.jgke.tagger.service.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.Model;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class PersonServiceTest {

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerPerson() {
        ArgumentCaptor<Person> varArgs = ArgumentCaptor.forClass(Person.class);
        String username = "user";
        String password = "pass";
        when(personRepository.save(varArgs.capture())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Person result = personService.registerPerson(username, password);
        Person p = varArgs.getValue();
        assertEquals(username, p.getUsername());
        assertEquals(BCrypt.hashpw(password, p.getSalt()), p.getPassword());
        assertEquals(result, p);
    }

    @PrepareForTest({SecurityContextHolder.class})
    @Test
    public void getAuthenticatedPerson() {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        String username = "user";
        Person person = new Person();
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(personRepository.findByUsername(username)).thenReturn(person);
        Person result = personService.getAuthenticatedPerson();
        assertEquals(result, person);
    }

    @PrepareForTest({SecurityContextHolder.class})
    @Test
    public void userCanModifyItsSource() {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        String username = "user";
        Person person = new Person();
        Source source = new Source();
        source.setPerson(person);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(personRepository.findByUsername(username)).thenReturn(person);
        assertTrue(personService.canCurrentUserModifySource(source));
    }

    @PrepareForTest({SecurityContextHolder.class})
    @Test
    public void anonymousUserCannotModifySource() {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Source source = new Source();
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("anonymousUser");
        assertTrue(!personService.canCurrentUserModifySource(source));
    }

    @PrepareForTest({SecurityContextHolder.class})
    @Test
    public void userCannotModifyOthersSource() {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        String username = "user";
        Person person = new Person();
        Source source = new Source();
        source.setPerson(new Person());
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(personRepository.findByUsername(username)).thenReturn(person);
        assertTrue(!personService.canCurrentUserModifySource(source));
    }

    @PrepareForTest({SecurityContextHolder.class})
    @Test
    public void adminCanModifyOthersSource() {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        String username = "user";
        Person person = new Person();
        person.setAdmin(true);
        Source source = new Source();
        source.setPerson(new Person());
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(personRepository.findByUsername(username)).thenReturn(person);
        assertTrue(personService.canCurrentUserModifySource(source));
    }
}
