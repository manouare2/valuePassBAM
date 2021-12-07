package com.example.karim.applicationfacteur.types;

import java.util.List;

/**
 * Created by hanane on 06/03/2019.
 */

public class AsyncResultEnvoi<T extends List<Envoi>> {


        public static final Void VOID_RESULT = (Void) null;

        private Exception exception;
        private T data;

        public  AsyncResultEnvoi(Exception exception) {
            this.exception = exception;
        }

        public  AsyncResultEnvoi(T data) {
            this.data = data;
        }

        public Exception getException() {
            return exception;
        }

        public List<Envoi> getData() {
            return data;
        }





}
