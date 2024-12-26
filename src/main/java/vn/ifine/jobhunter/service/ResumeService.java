package vn.ifine.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Resume;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResUpdateResumeDTO;

@Service
public interface ResumeService {
    boolean checkResumeExistByUserAndJob(Resume resume);

    ResCreateResumeDTO create(Resume resume);

    Optional<Resume> fetchById(long id);

    ResUpdateResumeDTO update(Resume resume);

    void delete(long id);

    ResFetchResumeDTO getResume(Resume resume);

    ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable);

    ResultPaginationDTO fetchResumeByUser(Pageable pageable);

}
