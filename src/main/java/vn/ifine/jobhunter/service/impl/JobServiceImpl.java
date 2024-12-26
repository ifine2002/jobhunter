package vn.ifine.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Company;
import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.Skill;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.ifine.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.ifine.jobhunter.repository.CompanyRepository;
import vn.ifine.jobhunter.repository.JobRepository;
import vn.ifine.jobhunter.repository.SkillRepository;
import vn.ifine.jobhunter.service.JobService;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobServiceImpl(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public ResCreateJobDTO create(Job j) {
        // check skills
        if (j.getSkills() != null) {
            List<Long> reqSkills = j.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            j.setSkills(dbSkills);
        }
        // check company
        if (j.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(j.getCompany().getId());
            if (companyOptional.isPresent()) {
                j.setCompany(companyOptional.get());
            }
        }

        // create job
        Job currentJob = this.jobRepository.save(j);

        // convert response
        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    @Override
    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    @Override
    public ResUpdateJobDTO update(Job j, Job jobInDB) {
        // check skills
        if (j.getSkills() != null) {
            List<Long> reqSkills = j.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            jobInDB.setSkills(dbSkills);
        }
        // check company
        if (j.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(j.getCompany().getId());
            if (companyOptional.isPresent()) {
                jobInDB.setCompany(companyOptional.get());
            }
        }

        // update correct info
        jobInDB.setName(j.getName());
        jobInDB.setSalary(j.getSalary());
        jobInDB.setQuantity(j.getQuantity());
        jobInDB.setLocation(j.getLocation());
        jobInDB.setLevel(j.getLevel());
        jobInDB.setStartDate(j.getStartDate());
        jobInDB.setEndDate(j.getEndDate());
        jobInDB.setActive(j.isActive());

        // update job
        Job currentJob = this.jobRepository.save(jobInDB);

        // convert response
        ResUpdateJobDTO res = new ResUpdateJobDTO();

        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());

        res.setUpdatedAt(currentJob.getUpdatedAt());
        res.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    @Override
    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO fetchAll(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());

        return rs;
    }

}
