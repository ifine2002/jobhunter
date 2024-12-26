package vn.ifine.jobhunter.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.ifine.jobhunter.domain.Company;
import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.Resume;
import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.ifine.jobhunter.service.ResumeService;
import vn.ifine.jobhunter.service.UserService;
import vn.ifine.jobhunter.util.SecurityUtil;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/admin")
public class ResumeManagementController {
    private final ResumeService resumeService;
    private final UserService userService;

    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeManagementController(ResumeService resumeService, UserService userService, FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    public ResponseEntity<ApiResponse<ResCreateResumeDTO>> create(@Valid @RequestBody Resume resume) throws Throwable {
        // check id exists
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User id/Job id không tồn tại");
        }
        // create new resume
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a CV", this.resumeService.create(resume)));
    }

    @PutMapping("/resumes")
    public ResponseEntity<ApiResponse<ResUpdateResumeDTO>> update(@RequestBody Resume resume)
            throws IdInvalidException {
        // check id exist
        Optional<Resume> resumeOptional = this.resumeService.fetchById(resume.getId());
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
        }

        Resume reqResume = resumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(ApiResponse.success("Update a resume", this.resumeService.update(reqResume)));
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id exist
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }

        // delete resume
        this.resumeService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success("Delete a resume by id", null));
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<ResFetchResumeDTO>> fetchById(@PathVariable("id") long id)
            throws IdInvalidException {
        // check id exist
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }

        return ResponseEntity.ok()
                .body(ApiResponse.success("Fetch a resume by id", this.resumeService.getResume(resumeOptional.get())));
    }

    @GetMapping("/resumes")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> fetchAll(@Filter Specification<Resume> spec,
            Pageable pageable) {
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handleUserByUsername(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(
                filterBuilder.field("job")
                        .in(filterBuilder.input(arrJobIds))
                        .get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Fetch all resumes", this.resumeService.fetchAllResume(finalSpec, pageable)));

    }

    @PostMapping("/resumes/by-user")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Get list resumes by user", this.resumeService.fetchResumeByUser(pageable)));
    }
}
