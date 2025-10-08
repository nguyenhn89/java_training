/*
 * AMELA Technology JSC
 *
 * NOTICE:  All source code, documentation and other information
 * contained herein is, and remains the property of Thor-Amela.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Thor-Amela.
 */
package org.example.java_training.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Thrown if request is invalid
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {
    
    private Object errorBody;
    
    
    /**
     * constructor.
     *
     * @param message Message
     */
    public BadRequestException(String message) {
        super(message);
    }
    
    /**
     * constructor.
     *
     * @param message Message
     * @param errorBody errorBody
     */
    public BadRequestException(String message, Object errorBody) {
        super(message);
        this.errorBody = errorBody;
    }
    
    /**
     * constructor.
     *
     * @param message   Message
     * @param exception Exception Object
     */
    public BadRequestException(String message, Exception exception) {
        super(message, exception);
    }
    
    /**
     * constructor.
     *
     * @param message   Message
     * @param errorBody errorBody
     * @param exception Exception Object
     */
    public BadRequestException(String message, Object errorBody, Exception exception) {
        super(message, exception);
        this.errorBody = errorBody;
    }
}
