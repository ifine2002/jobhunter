package vn.ifine.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.repository.UserRepository;
import vn.ifine.jobhunter.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
        ;
    }

    @Override
    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    @Override
    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }

    @Override
    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setName(reqUser.getName());
            currentUser.setAge(reqUser.getAge());
            currentUser.setGender(reqUser.getGender());

            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public User handleUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}
