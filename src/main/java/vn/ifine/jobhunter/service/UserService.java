package vn.ifine.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.User;

@Service
public interface UserService {
    User handleCreateUser(User user);

    void handleDeleteUser(long id);

    User fetchUserById(long id);

    List<User> fetchAllUser();

    User handleUpdateUser(User user);

    User handleUserByUsername(String username);
}
