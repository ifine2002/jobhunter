package vn.ifine.jobhunter.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.ifine.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.ifine.jobhunter.domain.response.user.ResUserDTO;
import vn.ifine.jobhunter.service.UserService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

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
    public ResponseEntity<ApiResponse<ResCreateUserDTO>> createUser(@Valid @RequestBody User reqUser)
            throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(reqUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + reqUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(hashPassword);
        User user = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a new user successfully",
                        this.userService.convertToResCreateUserDTO(user)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity
                .ok(ApiResponse.success("Delete user successfully", null));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<ResUserDTO>> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(
                ApiResponse.success("Fetch a user successfully", this.userService.convertToResUserDTO(currentUser)));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllUser(@Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity
                .ok(ApiResponse.success("Fetch all user successfully", this.userService.fetchAllUser(spec, pageable)));
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<ResUpdateUserDTO>> updateUser(@RequestBody User reqUser)
            throws IdInvalidException {
        User user = this.userService.handleUpdateUser(reqUser);
        if (user == null) {
            throw new IdInvalidException("User với id = " + reqUser.getId() + " không tồn tại");
        }
        return ResponseEntity
                .ok(ApiResponse.success("Fetch a user successfully", this.userService.convertToResUpdateUserDTO(user)));
    }

}
