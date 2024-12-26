package vn.ifine.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Role;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;

@Service
public interface RoleService {
    boolean existByName(String name);

    Role create(Role role);

    Role fetchById(long id);

    Role update(Role role);

    void delete(long id);

    ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable);
}
