package com.example.karim.applicationfacteur.types;

import java.util.List;

public class AsyncResult1<T extends List<Agency3>> {

    public static final Void VOID_RESULT = (Void) null;

    private Exception exception;
    private T data;

    public AsyncResult1(Exception exception) {
        this.exception = exception;
    }

    public AsyncResult1(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public List<Agency3> getData() {
        return data;
    }

}
