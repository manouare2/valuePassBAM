package com.example.karim.applicationfacteur.types;

import java.util.List;

public class AsyncResult<T extends List<Agency>> {

    public static final Void VOID_RESULT = (Void) null;

    private Exception exception;
    private T data;

    public AsyncResult(Exception exception) {
        this.exception = exception;
    }

    public AsyncResult(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public List<Agency> getData() {
        return data;
    }

}
