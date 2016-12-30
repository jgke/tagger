package fi.jgke.tagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Source not found")
public class SourceNotFoundException extends RuntimeException {
}
