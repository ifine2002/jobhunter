package vn.ifine.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.ifine.jobhunter.domain.response.job.ResUpdateJobDTO;

@Service
public interface JobService {
    ResCreateJobDTO create(Job j);

    Optional<Job> fetchJobById(long id);

    ResUpdateJobDTO update(Job j, Job jobInDB);

    void delete(long id);

    ResultPaginationDTO fetchAll(Specification<Job> spec, Pageable pageable);
}
