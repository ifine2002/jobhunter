package vn.ifine.jobhunter.controller.client;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.service.JobService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/public")
public class JobController {
    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
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
