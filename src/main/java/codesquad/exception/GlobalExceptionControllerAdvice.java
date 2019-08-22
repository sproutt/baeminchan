package codesquad.exception;

import codesquad.dto.ErrorResponse;
import codesquad.dto.ValidateError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionControllerAdvice {
    private Logger log = LoggerFactory.getLogger(GlobalExceptionControllerAdvice.class);
    private final MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public GlobalExceptionControllerAdvice(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("handleMethodArgumentNotValidException : {}", e);

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value());

        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        for (ObjectError objectError : errors) {
            FieldError fieldError = (FieldError) objectError;

            response.addErrorMessage(new ValidateError(fieldError.getField(), getErrorMessage(fieldError)));
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(FieldError error) {
        Optional<String> code = getFirstCode(error);
        if (!code.isPresent()) {
            return null;
        }

        String errorMessage = messageSourceAccessor.getMessage(code.get(), error.getArguments(), error.getDefaultMessage());
        log.info("error code: {}", code.get());
        log.info("error message: {}", errorMessage);
        return errorMessage;
    }

    private Optional<String> getFirstCode(ObjectError error) {
        String[] codes = error.getCodes();
        if (codes == null || codes.length == 0) {
            return Optional.empty();
        }
        return Optional.of(codes[0]);
    }

    @ExceptionHandler(value = UnMatchedCheckingPasswordException.class)
    public ResponseEntity<ErrorResponse> handleUnMatchedCheckingPasswordException(UnMatchedCheckingPasswordException e) {
        log.error("handleUnMatchedCheckingPasswordException : {}", e);

        return makeResponseEntity(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(value = DuplicatedAccountException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedAccountException(DuplicatedAccountException e) {
        log.error("handleDuplicatedAccountException : {}", e);

        return makeResponseEntity(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(value = NotFoundAccountException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundAccountException(NotFoundAccountException e) {
        log.error("handleNotFoundAccountException : {}", e);

        return makeResponseEntity(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(value = UnAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticationException(UnAuthenticationException e) {
        log.error("handleUnAuthenticationException : {}", e);

        return makeResponseEntity(HttpStatus.FORBIDDEN, e);
    }

    private ResponseEntity<ErrorResponse> makeResponseEntity(HttpStatus status, Exception e) {
        ErrorResponse response = new ErrorResponse(status.value());
        response.addErrorMessage(new ValidateError("" ,e.getMessage()));

        log.debug("check : {}", e);

        return new ResponseEntity<>(response, status);
    }
}
