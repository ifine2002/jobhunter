package vn.ifine.jobhunter.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User reqUser) {
        String hashPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(hashPassword);
        User user = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a new user successfully", user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Delete user successfully", null));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok(ApiResponse.success("Fetch a user successfully", this.userService.fetchUserById(id)));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUser() {
        return ResponseEntity.ok(ApiResponse.success("Fetch all user successfully", this.userService.fetchAllUser()));
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User reqUser) {
        User user = this.userService.handleUpdateUser(reqUser);
        return ResponseEntity.ok(ApiResponse.success("Fetch a user successfully", user));
    }

}
