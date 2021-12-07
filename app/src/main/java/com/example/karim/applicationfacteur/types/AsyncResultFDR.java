package com.example.karim.applicationfacteur.types;

import java.util.List;

public class AsyncResultFDR<T extends List<Collecte>> {

    public static final Void VOID_RESULT = (Void) null;

    private Exception exception;
    private T data;

    public AsyncResultFDR(Exception exception) {
        this.exception = exception;
    }

    public AsyncResultFDR(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public List<Collecte> getData() {
        return data;
    }
}
