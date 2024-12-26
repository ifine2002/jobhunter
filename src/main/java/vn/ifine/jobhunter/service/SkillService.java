package vn.ifine.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Skill;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;

@Service
public interface SkillService {
    boolean isNameExist(String name);

    Skill createSkill(Skill skill);

    Skill fetchSkillById(long id);

    Skill update(Skill s);

    ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable);

    void deleteSkill(long id);
}
