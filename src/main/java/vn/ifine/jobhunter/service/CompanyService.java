package vn.ifine.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Company;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;

@Service
public interface CompanyService {

    Company handleCreateCompany(Company reqCompany);

    ResultPaginationDTO fetchAllCompany(Specification<Company> spec, Pageable pageable);

    Company handleUpdateCompany(Company reqCompany);

    void handleDeleteCompany(long id);

    Optional<Company> findById(long id);

    boolean isNameExist(String name);
}
