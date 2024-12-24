package vn.ifine.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.ifine.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.ifine.jobhunter.domain.response.user.ResUserDTO;

@Service
public interface UserService {
    User handleCreateUser(User user);

    void handleDeleteUser(long id);

    User fetchUserById(long id);

    ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable);

    User handleUpdateUser(User user);

    User handleUserByUsername(String username);

    boolean isEmailExist(String email);

    ResCreateUserDTO convertToResCreateUserDTO(User user);

    ResUserDTO convertToResUserDTO(User user);

    ResUpdateUserDTO convertToResUpdateUserDTO(User user);

    void updateUserToken(String token, String email);

    User getUserByRefreshAndEmail(String token, String email);
}
