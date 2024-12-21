package vn.ifine.jobhunter.controller.client;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ifine.jobhunter.domain.request.ReqLoginDTO;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.user.ResLoginDTO;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<ReqLoginDTO>> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Login successfully", loginDTO));
    }
}
