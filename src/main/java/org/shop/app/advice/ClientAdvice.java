package org.shop.app.advice;

import org.shop.app.exception.*;
import org.shop.app.util.ErrorMapperUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ClientAdvice {

    @ExceptionHandler({
            ClientAlreadyCreatedException.class,
            ClientUnableToFindException.class,
            ClientAlreadyDeletedException.class,
            NotMyOrderToPayException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleClientExceptions(RuntimeException e) {
        List<String> errorsList = Collections.singletonList(e.getMessage());

        return new ResponseEntity<>(ErrorMapperUtil.getErrorsMap(errorsList),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }



}
