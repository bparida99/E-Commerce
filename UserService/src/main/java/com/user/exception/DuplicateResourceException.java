package com.user.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateResourceException extends DataIntegrityViolationException {

    public DuplicateResourceException(String msg) {
        super(msg);
    }
}
