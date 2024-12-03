
package com.netsflow.unitransit.exception;

public class ExpiredOTPException extends RuntimeException {
    public ExpiredOTPException(String message) {
        super(message);
    }
}
