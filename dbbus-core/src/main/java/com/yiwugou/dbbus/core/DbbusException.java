package com.yiwugou.dbbus.core;

public class DbbusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DbbusException() {
        super();
    }

    public DbbusException(String message) {
        super(message);
    }

    public DbbusException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbbusException(Throwable cause) {
        super(cause);
    }

}
