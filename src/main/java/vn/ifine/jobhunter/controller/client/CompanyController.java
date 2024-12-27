package vn.ifine.jobhunter.controller.client;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.ifine.jobhunter.domain.Company;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.service.CompanyService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/public")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllCompany(
            @Filter Specification<Company> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Fetch all company successfully",
                this.companyService.fetchAllCompany(spec, pageable)));
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
