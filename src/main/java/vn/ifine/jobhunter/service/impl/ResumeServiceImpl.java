package vn.ifine.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.Resume;
import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.ifine.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.ifine.jobhunter.repository.JobRepository;
import vn.ifine.jobhunter.repository.ResumeRepository;
import vn.ifine.jobhunter.repository.UserRepository;
import vn.ifine.jobhunter.service.ResumeService;
import vn.ifine.jobhunter.util.SecurityUtil;

@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public boolean checkResumeExistByUserAndJob(Resume resume) {
        // check user by id
        if (resume.getUser() == null)
            return false;
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }

        // check job by id
        if (resume.getJob() == null)
            return false;
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume.getId());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());

        return res;
    }

    @Override
    public Optional<Resume> fetchById(long id) {
        return this.resumeRepository.findById(id);
    }

    @Override
    public ResUpdateResumeDTO update(Resume resume) {
        resume = this.resumeRepository.save(resume);

        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        return res;
    }

    @Override
    public void delete(long id) {
        this.resumeRepository.deleteById(id);
    }

    @Override
    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();

        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return res;
    }

    @Override
    public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);
        return rs;
    }

    @Override
    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }

}
