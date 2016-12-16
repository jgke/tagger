package fi.jgke.tagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Tag already exists on source")  // 404
public class TagAlreadyExistsException extends RuntimeException {
}
