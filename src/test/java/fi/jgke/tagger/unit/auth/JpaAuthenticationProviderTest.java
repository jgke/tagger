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
package fi.jgke.tagger.unit.auth;

import fi.jgke.tagger.auth.JpaAuthenticationProvider;
import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.repository.PersonRepository;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author jgke
 */
public class JpaAuthenticationProviderTest {

    @InjectMocks
    JpaAuthenticationProvider jpaAuthenticationProvider;

    @Mock
    PersonRepository personRepository;

    @Mock
    Authentication authentication;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSuccesfulAuthentication() {
        String username = "test_user";
        String password = "test_pass";
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        person.setAdmin(false);

        Mockito.when(authentication.getPrincipal()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.when(personRepository.findByUsername(username)).thenReturn(person);
        Authentication auth = jpaAuthenticationProvider.authenticate(authentication);

        assertEquals(auth.getPrincipal(), username);
        assertEquals(auth.getCredentials(), password);
        assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("USER")));
        assertFalse(auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    public void testSuccesfulAdminAuthentication() {
        String username = "test_user";
        String password = "test_pass";
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        person.setAdmin(true);

        Mockito.when(authentication.getPrincipal()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.when(personRepository.findByUsername(username)).thenReturn(person);
        Authentication auth = jpaAuthenticationProvider.authenticate(authentication);

        assertEquals(auth.getPrincipal(), username);
        assertEquals(auth.getCredentials(), password);
        assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("USER")));
        assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
    }


    @Test(expected = AuthenticationException.class)
    public void testNonexistingUsername() {
        String username = "test_user";
        String password = "test_pass";

        Mockito.when(authentication.getPrincipal()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.when(personRepository.findByUsername(username)).thenReturn(null);
        jpaAuthenticationProvider.authenticate(authentication);
    }

    @Test(expected = AuthenticationException.class)
    public void testWrongPassword() {
        String username = "test_user";
        String password = "test_pass";
        String real_password = "other_pass";
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(real_password);
        person.setAdmin(false);

        Mockito.when(authentication.getPrincipal()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.when(personRepository.findByUsername(username)).thenReturn(person);
        Authentication auth = jpaAuthenticationProvider.authenticate(authentication);
    }

    @Test
    public void testSupports() {
        JpaAuthenticationProvider instance = new JpaAuthenticationProvider();
        assertTrue(instance.supports(AuthenticationProvider.class));
    }

}
