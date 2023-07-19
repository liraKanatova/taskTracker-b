package peaksoft.house.tasktrackerb9.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;
import peaksoft.house.tasktrackerb9.exception.AlreadyExistException;
import peaksoft.house.tasktrackerb9.exception.BadCredentialException;
import peaksoft.house.tasktrackerb9.exception.BadRequestException;
import peaksoft.house.tasktrackerb9.exception.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ExceptionResponse handleNotFoundException(NotFoundException e){
            return  new ExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        @ExceptionHandler(AlreadyExistException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ExceptionResponse handleNotAlreadyExist(AlreadyExistException e){
            return  new ExceptionResponse(
                    HttpStatus.CONFLICT,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        @ExceptionHandler(BadCredentialException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionResponse handleBadRequest(BadRequestException e){
            return  new ExceptionResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        @ExceptionHandler(BadCredentialException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ExceptionResponse handleBadCredentialException(BadCredentialException e){
            return  new ExceptionResponse(
                    HttpStatus.FORBIDDEN,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

    }
