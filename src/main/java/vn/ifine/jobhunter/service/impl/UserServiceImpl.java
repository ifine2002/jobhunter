package vn.ifine.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.ifine.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.ifine.jobhunter.domain.response.user.ResUserDTO;
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
    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {

        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
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

    @Override
    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        // ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        // if (user.getCompany() != null) {
        // com.setId(user.getCompany().getId());
        // com.setName(user.getCompany().getName());
        // res.setCompany(com);
        // }

        return res;
    }

    @Override
    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        // ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        // if (user.getRole() != null) {
        // roleUser.setId(user.getRole().getId());
        // roleUser.setName(user.getRole().getName());
        // res.setRole(roleUser);
        // }

        // if (user.getCompany() != null) {
        // com.setId(user.getCompany().getId());
        // com.setName(user.getCompany().getName());
        // res.setCompany(com);
        // }

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;
    }

    @Override
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        // ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();
        // if (user.getCompany() != null) {
        // com.setId(user.getCompany().getId());
        // com.setName(user.getCompany().getName());
        // res.setCompany(com);
        // }
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = this.handleUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

}
