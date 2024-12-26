package vn.ifine.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Permission;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;

@Service
public interface PermissionService {
    boolean existsByModuleAndApiPathAndMethod(Permission p);

    Permission create(Permission p);

    Permission fetchPermissionById(long id);

    Permission update(Permission permission);

    void delete(long id);

    ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable);

    boolean isSameName(Permission p);
}
