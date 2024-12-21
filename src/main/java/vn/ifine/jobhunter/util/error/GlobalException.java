package vn.ifine.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import vn.ifine.jobhunter.domain.response.ApiResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = { UsernameNotFoundException.class, IdInvalidException.class })
    public ResponseEntity<ApiResponse<Object>> handleIdException(Exception ex) {
        // Sử dụng Builder Pattern của Lombok
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Exception occurs...")
                .message(ex.getMessage())
                .data(null) // Trong trường hợp không có dữ liệu trả về
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    // xử lý lỗi truyền sai định dạng PathVariable
    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(Exception ex) {
        // Sử dụng Builder Pattern của Lombok
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Incorrect data type")
                .message(ex.getMessage())
                .data(null) // Trong trường hợp không có dữ liệu trả về
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    // xử lý lỗi khi valid dữ liệu (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getBody().getDetail())
                .message(errors.size() > 1 ? errors : errors.get(0))
                .data(null) // Trong trường hợp không có dữ liệu trả về
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
