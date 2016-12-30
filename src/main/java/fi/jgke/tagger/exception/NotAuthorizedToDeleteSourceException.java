package fi.jgke.tagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Not authorized to delete source")
public class NotAuthorizedToDeleteSourceException extends RuntimeException {
}
