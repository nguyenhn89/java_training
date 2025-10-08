/*
 * AMELA Technology JSC
 *
 * NOTICE:  All source code, documentation and other information
 * contained herein is, and remains the property of Thor-Amela.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Thor-Amela.
 */
package org.example.java_training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Http exception return 404
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class HttpNotFoundException extends RuntimeException {
    
    /**
     * Constructor
     * @param message message
     */
    public HttpNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor
     * @param message message
     * @param throwable throwable
     */
    public HttpNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
