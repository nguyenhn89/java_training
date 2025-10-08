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
 * Http exception return 400
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HttpBadRequestException extends RuntimeException {
    
    /**
     * Constructor
     * @param message message
     */
    public HttpBadRequestException(String message) {
        super(message);
    }
    
    /**
     * Constructor
     * @param message message
     * @param throwable throwable
     */
    public HttpBadRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
