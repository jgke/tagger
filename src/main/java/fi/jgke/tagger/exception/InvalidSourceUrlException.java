package fi.jgke.tagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Malformed source URL")  // 404
public class InvalidSourceUrlException extends RuntimeException {
}
