package vn.ifine.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Permission;
import vn.ifine.jobhunter.domain.Role;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.repository.PermissionRepository;
import vn.ifine.jobhunter.repository.RoleRepository;
import vn.ifine.jobhunter.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    @Override
    public Role create(Role role) {
        // check permissions
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(role);
    }

    @Override
    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    @Override
    public Role update(Role role) {
        Role roleDB = this.fetchById(role.getId());
        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermisssions = role.getPermissions().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermisssions);
            role.setPermissions(dbPermissions);
        }

        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.isActive());
        roleDB.setPermissions(role.getPermissions());
        roleDB = this.roleRepository.save(roleDB);
        return roleDB;
    }

    @Override
    public void delete(long id) {
        // delete role inside user
        Role role = this.fetchById(id);
        role.getUsers().forEach(user -> user.setRole(null));
        this.roleRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageRole.getContent());

        return rs;

    }

}
