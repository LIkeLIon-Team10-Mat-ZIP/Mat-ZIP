package site.matzip.base.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends Exception {
    protected HttpStatus httpStatus;

    public UnauthorizedException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
