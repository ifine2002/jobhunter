package vn.ifine.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.ifine.jobhunter.domain.response.ApiResponse;

@RestControllerAdvice
public class GlobalException {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleAllException(Exception ex) {
                // Sử dụng Builder Pattern của Lombok
                ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error("Internal Server Error")
                                .message(ex.getMessage())
                                .data(null) // Trong trường hợp không có dữ liệu trả về
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }

        @ExceptionHandler(value = { UsernameNotFoundException.class, IdInvalidException.class,
                        BadCredentialsException.class })
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
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ApiResponse<Object>> validationError(MethodArgumentNotValidException ex) {
                BindingResult result = ex.getBindingResult();
                final List<FieldError> fieldErrors = result.getFieldErrors();
                List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).toList();
                ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(ex.getBody().getDetail())
                                .message(errors.size() > 1 ? errors : errors.get(0))
                                .data(null) // Trong trường hợp không có dữ liệu trả về
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        // xử lý 404 not found
        @ExceptionHandler(value = {
                        NoResourceFoundException.class,
        })
        public ResponseEntity<ApiResponse<Object>> handleNotFoundException(Exception ex) {
                // Sử dụng Builder Pattern của Lombok
                ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .error("404 Not Found. URL may not exist...")
                                .message(ex.getMessage())
                                .data(null) // Trong trường hợp không có dữ liệu trả về
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }

        @ExceptionHandler(value = {
                        StorageException.class })
        public ResponseEntity<ApiResponse<Object>> handleFileUploadException(Exception ex) {
                // Sử dụng Builder Pattern của Lombok
                ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Exception upload file...")
                                .message(ex.getMessage())
                                .data(null) // Trong trường hợp không có dữ liệu trả về
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        @ExceptionHandler(value = {
                        PermissionException.class })
        public ResponseEntity<ApiResponse<Object>> handlePermissionException(Exception ex) {
                // Sử dụng Builder Pattern của Lombok
                ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .error("Forbidden")
                                .message(ex.getMessage())
                                .data(null) // Trong trường hợp không có dữ liệu trả về
                                .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
        }
}
