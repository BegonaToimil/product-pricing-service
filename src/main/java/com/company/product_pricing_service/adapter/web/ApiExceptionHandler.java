package com.company.product_pricing_service.adapter.web;

import com.company.product_pricing_service.domain.PriceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Converts a missing applicable price into a 404 Problem Detail response.
     *
     * @param exception price lookup exception
     * @return 404 problem response
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ProblemDetail handlePriceNotFound(PriceNotFoundException exception) {
        log.warn("No applicable price found: {}", exception.getMessage());
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Price not found");
        return problem;
    }

    /**
     * Converts missing or invalid request parameters into a 400 Problem Detail response.
     *
     * @param exception validation or binding exception
     * @return 400 problem response
     */
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HandlerMethodValidationException.class
    })
    public ProblemDetail handleBadRequest(Exception exception) {
        log.warn("Rejected request with missing or invalid parameters");
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more request parameters are missing or invalid."
        );
        problem.setTitle("Invalid request");
        return problem;
    }

    /**
     * Converts a persistence failure into a safe 500 Problem Detail response.
     *
     * @param exception persistence exception
     * @return 500 problem response
     */
    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handlePersistenceFailure(DataAccessException exception) {
        log.error("Price persistence operation failed", exception);
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "The price could not be retrieved due to an internal error."
        );
        problem.setTitle("Internal server error");
        return problem;
    }

    /**
     * Preserves framework HTTP errors or converts unexpected failures into 500 responses.
     *
     * @param exception unexpected or framework exception
     * @return corresponding HTTP problem response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedFailure(Exception exception) {
        if (exception instanceof ErrorResponse errorResponse) {
            return ResponseEntity.status(errorResponse.getStatusCode())
                    .headers(errorResponse.getHeaders())
                    .body(errorResponse.getBody());
        }

        log.error("Unexpected error while handling request", exception);
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred."
        );
        problem.setTitle("Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
