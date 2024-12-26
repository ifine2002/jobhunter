package vn.ifine.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Company;
import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.repository.CompanyRepository;
import vn.ifine.jobhunter.repository.JobRepository;
import vn.ifine.jobhunter.repository.UserRepository;
import vn.ifine.jobhunter.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public Company handleCreateCompany(Company reqCompany) {
        return this.companyRepository.save(reqCompany);
    }

    @Override
    public ResultPaginationDTO fetchAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    @Override
    public Company handleUpdateCompany(Company reqCompany) {
        Optional<Company> companyOptional = this.companyRepository.findById(reqCompany.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setName(reqCompany.getName());
            currentCompany.setAddress(reqCompany.getAddress());
            currentCompany.setLogo(reqCompany.getLogo());
            currentCompany.setDescription(reqCompany.getDescription());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    @Override
    public void handleDeleteCompany(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company com = companyOptional.get();
            // fetch all user belong to this company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
            // fetch all job belong to this company
            List<Job> jobs = this.jobRepository.findByCompany(com);
            this.jobRepository.deleteAll(jobs);

        }
        this.companyRepository.deleteById(id);
    }

    @Override
    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }

    @Override
    public boolean isNameExist(String name) {
        return this.companyRepository.existsByName(name);
    }

}
