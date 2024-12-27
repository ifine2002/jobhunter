package vn.ifine.jobhunter.controller.client;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.ifine.jobhunter.domain.Resume;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.ifine.jobhunter.service.ResumeService;
import vn.ifine.jobhunter.service.UserService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/auth")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;

    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService, FilterBuilder filterBuilder,
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

    @PostMapping("/resumes/by-user")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Get list resumes by user", this.resumeService.fetchResumeByUser(pageable)));
    }
}