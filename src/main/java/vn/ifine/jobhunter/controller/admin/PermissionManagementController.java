package vn.ifine.jobhunter.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import vn.ifine.jobhunter.domain.Permission;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.service.PermissionService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/admin")
public class PermissionManagementController {

    private final PermissionService permissionService;

    public PermissionManagementController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<Permission>> create(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        // check exist permission
        boolean isExist = this.permissionService.existsByModuleAndApiPathAndMethod(permission);
        if (isExist) {
            throw new IdInvalidException("Permission đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a permission", this.permissionService.create(permission)));
    }

    @PutMapping("/permissions")
    public ResponseEntity<ApiResponse<Permission>> update(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        // check exist by id
        if (this.permissionService.fetchPermissionById(permission.getId()) == null) {
            throw new IdInvalidException("Permission với id = " + permission.getId() + " không tồn tại");
        }

        // check exist by module, apiPath and method
        if (this.permissionService.existsByModuleAndApiPathAndMethod(permission)) {
            // check name
            if (this.permissionService.isSameName(permission)) {
                throw new IdInvalidException("Permission đã tồn tại");
            }
        }
        return ResponseEntity.ok()
                .body(ApiResponse.success("Update a permission", this.permissionService.update(permission)));
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check exist by id
        if (this.permissionService.fetchPermissionById(id) == null) {
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success("Delete a permission", null));
    }

    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(
                ApiResponse.success("Fetch all permission", this.permissionService.getPermissions(spec, pageable)));
    }
}
