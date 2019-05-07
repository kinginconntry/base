package com.needto.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 含有多个分开的错误信息
 */
public class Errors extends Result {

    public static class Error {
        public String message;
        public String error;

        public Error(String message, String error) {
            this.message = message;
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    public List<Error> errorList;

    public Errors() {
        this.success = false;
        this.errorList = new ArrayList<>();
    }

    public void addError(String error, String message){
        this.errorList.add(new Error(message, error));
    }

    public List<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Error> errorList) {
        this.errorList = errorList;
    }
}
