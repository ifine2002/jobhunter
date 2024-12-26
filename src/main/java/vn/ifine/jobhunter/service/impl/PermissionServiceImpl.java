package vn.ifine.jobhunter.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Permission;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.repository.PermissionRepository;
import vn.ifine.jobhunter.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean existsByModuleAndApiPathAndMethod(Permission p) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(p.getModule(), p.getApiPath(),
                p.getMethod());
    }

    @Override
    public Permission create(Permission p) {
        return this.permissionRepository.save(p);
    }

    @Override
    public Permission fetchPermissionById(long id) {
        Optional<Permission> permissOptional = this.permissionRepository.findById(id);
        if (permissOptional.isPresent()) {
            return permissOptional.get();
        }
        return null;
    }

    @Override
    public Permission update(Permission permission) {
        Permission permissionDB = this.fetchPermissionById(permission.getId());
        if (permissionDB != null) {
            permissionDB.setName(permission.getName());
            permissionDB.setApiPath(permission.getApiPath());
            permissionDB.setMethod(permission.getMethod());
            permissionDB.setModule(permission.getModule());

            // update
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        // delete permission_role
        Permission currentPermission = this.fetchPermissionById(id);
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete permission
        this.permissionRepository.delete(currentPermission);
    }

    @Override
    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pagePermission.getContent());

        return rs;
    }

    @Override
    public boolean isSameName(Permission p) {
        Permission permissionDB = this.fetchPermissionById(p.getId());
        if (permissionDB != null) {
            if (permissionDB.getName().equals(p.getName()))
                return true;
        }
        return false;
    }

}
