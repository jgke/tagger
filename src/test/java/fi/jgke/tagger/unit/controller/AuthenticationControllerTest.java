/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.jgke.tagger.unit.controller;

import fi.jgke.tagger.controller.AuthenticationController;
import fi.jgke.tagger.domain.Person;
import fi.jgke.tagger.repository.PersonRepository;
import fi.jgke.tagger.service.PersonService;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

/**
 *
 * @author jgke
 */
public class AuthenticationControllerTest {

    public AuthenticationControllerTest() {
    }

    @InjectMocks
    AuthenticationController authenticationController;

    @Mock
    PersonRepository personRepository;

    @Mock
    PersonService personService;

    @Mock
    Model model;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin() {
        assertEquals("login", authenticationController.login(model, null, null));
        Mockito.verifyNoMoreInteractions(model);
    }

    @Test
    public void testFailedLogin() {
        assertEquals("login", authenticationController.login(model, "true", null));
        Mockito.verify(model).addAttribute("loginError", true);
        Mockito.verifyNoMoreInteractions(model);
    }

    @Test
    public void testSuccessfulRegistration() {
        assertEquals("login", authenticationController.login(model, null, "true"));
        Mockito.verify(model).addAttribute("registerSuccess", true);
        Mockito.verifyNoMoreInteractions(model);
    }

    @Test
    public void testSuccesfulRegisterForm() {
        assertEquals("register", authenticationController.registerForm(model, null));
        Mockito.verifyNoMoreInteractions(model);
    }

    @Test
    public void testDuplicateRegisterForm() {
        assertEquals("register", authenticationController.registerForm(model, "true"));
        Mockito.verify(model).addAttribute("registerDuplicate", true);
        Mockito.verifyNoMoreInteractions(model);
    }

    @Test
    public void testSuccesfulRegister() throws Exception {
        String username = "test_username";
        String password = "test_password";
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        Mockito.when(personRepository.findByUsername(username)).thenReturn(null);
        Mockito.when(personService.registerPerson(username, password)).thenReturn(person);
        assertEquals("redirect:/login?success", authenticationController.register(username, password));
    }

    @Test
    public void testDuplicateRegister() throws Exception {
        String username = "test_username";
        String password = "test_password";
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        Mockito.when(personRepository.findByUsername(username)).thenReturn(person);
        assertEquals("redirect:/register?duplicate", authenticationController.register(username, password));
    }
}
