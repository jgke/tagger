package fi.jgke.tagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already exists")
public class UsernameAlreadyExistsException extends RuntimeException {
}
