package vn.ifine.jobhunter.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import vn.ifine.jobhunter.domain.response.ApiResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = { IdInvalidException.class })
    public ResponseEntity<ApiResponse<Object>> handleIdException(Exception ex) {
        // Sử dụng Builder Pattern của Lombok
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
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
                .message(ex.getMessage())
                .data(null) // Trong trường hợp không có dữ liệu trả về
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
