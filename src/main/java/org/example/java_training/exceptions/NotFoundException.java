/*
 * AMELA Technology JSC
 *
 * NOTICE:  All source code, documentation and other information
 * contained herein is, and remains the property of Thor-Amela.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Thor-Amela.
 */
package org.example.java_training.exceptions;

/**
 * Thrown if resource is not found
 */
@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
    
    /**
     * constructor.
     *
     * @param message Message
     */
    public NotFoundException(String message) {
        super(message);
    }
    
    /**
     * constructor.
     *
     * @param message   Message
     * @param exception Exception Object
     */
    public NotFoundException(String message, Exception exception) {
        super(message, exception);
    }
    
}
