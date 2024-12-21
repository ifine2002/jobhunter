package vn.ifine.jobhunter.controller.admin;

import java.util.Optional;

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
import vn.ifine.jobhunter.domain.Company;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.service.CompanyService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/admin")
public class CompanyManagementController {

    private final CompanyService companyService;

    public CompanyManagementController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<ApiResponse<Company>> createNewCompany(@Valid @RequestBody Company reqCompany)
            throws IdInvalidException {
        boolean isNameExist = this.companyService.isNameExist(reqCompany.getName());
        if (isNameExist) {
            throw new IdInvalidException(
                    "Company với name = " + reqCompany.getName() + " đã tồn tại, vui lòng sử dụng tên khác");
        }
        Company newCompany = this.companyService.handleCreateCompany(reqCompany);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a company user successfully", newCompany));
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllCompany(
            @Filter Specification<Company> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Fetch all company successfully",
                this.companyService.fetchAllCompany(spec, pageable)));
    }

    @PutMapping("/companies")
    public ResponseEntity<ApiResponse<Company>> updateCompany(@Valid @RequestBody Company reqCompany)
            throws IdInvalidException {
        Company updatedCompany = this.companyService.handleUpdateCompany(reqCompany);
        if (updatedCompany == null) {
            throw new IdInvalidException("Company với id = " + reqCompany.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(ApiResponse.success("Update a company successfully", updatedCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Company> companyOptional = this.companyService.findById(id);
        if (!companyOptional.isPresent()) {
            throw new IdInvalidException("Company với id = " + id + " không tồn tại");
        }
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok().body(ApiResponse.success("Delete a company successfully", null));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<ApiResponse<Company>> fetchCompanyById(@PathVariable("id") long id)
            throws IdInvalidException {
        Optional<Company> companyOptional = this.companyService.findById(id);
        if (!companyOptional.isPresent()) {
            throw new IdInvalidException("Company với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(ApiResponse.success("Fetch a company successfully", companyOptional.get()));
    }
}
