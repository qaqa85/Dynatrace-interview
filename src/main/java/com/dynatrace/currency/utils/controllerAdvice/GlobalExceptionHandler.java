package com.dynatrace.currency.utils.controllerAdvice;

import com.dynatrace.currency.nbpClient.exceptions.NoDataException;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import com.dynatrace.currency.utils.exceptions.InvalidDateException;
import com.dynatrace.currency.utils.exceptions.InvalidLastQuotationsNumberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidCurrencyCodeException.class,
            InvalidLastQuotationsNumberException.class,
            InvalidDateException.class
    })
    ResponseEntity<String> inputExceptions(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({NoDataException.class})
    ResponseEntity<String> nbpClientExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
