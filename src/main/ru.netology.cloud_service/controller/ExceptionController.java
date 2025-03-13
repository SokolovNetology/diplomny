package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.cloud_service.exception.BadCredentialsException;
import ru.netology.cloud_service.exception.DeleteFileException;
import ru.netology.cloud_service.exception.InputDataException;
import ru.netology.cloud_service.exception.UploadFileException;
import ru.netology.cloud_service.model.dtos.response.ExceptionResponse;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ExceptionResponse> handleInputData(InputDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage(), 400));
    }

    @ExceptionHandler(DeleteFileException.class)
    public ResponseEntity<ExceptionResponse> handleDeleteFile(DeleteFileException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(e.getMessage(), 500));
    }

    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<ExceptionResponse> handleUploadFile(UploadFileException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(e.getMessage(), 500));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage(), 400));
    }
}
