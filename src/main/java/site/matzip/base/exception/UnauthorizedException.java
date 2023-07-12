package site.matzip.base.exception;

public class UnauthorizedException extends Exception {
    private String msg;

    public UnauthorizedException(String msg) {
        this.msg = msg;
    }
}
