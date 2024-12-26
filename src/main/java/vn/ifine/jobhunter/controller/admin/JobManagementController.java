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
import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.ifine.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.ifine.jobhunter.service.JobService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/admin")
public class JobManagementController {
    private JobService jobService;

    public JobManagementController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<ResCreateJobDTO>> create(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a new job", this.jobService.create(job)));
    }

    @PutMapping("/jobs")
    public ResponseEntity<ApiResponse<ResUpdateJobDTO>> update(@Valid @RequestBody Job job) throws IdInvalidException {
        // check job có tồn tại
        Optional<Job> jobOptional = this.jobService.fetchJobById(job.getId());
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok()
                .body(ApiResponse.success("Update a job", this.jobService.update(job, jobOptional.get())));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check job có tồn tại
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.delete(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success("Update a job", null));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllJob(
            @Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Fetch all jobs", this.jobService.fetchAll(spec, pageable)));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Job>> getJob(@PathVariable("id") long id) throws IdInvalidException {
        // check job có tồn tại
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok()
                .body(ApiResponse.success("Fetch a job", jobOptional.get()));
    }

}
