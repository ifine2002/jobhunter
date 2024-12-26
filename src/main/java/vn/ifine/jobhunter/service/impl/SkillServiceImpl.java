package vn.ifine.jobhunter.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Skill;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.repository.SkillRepository;
import vn.ifine.jobhunter.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    @Override
    public Skill createSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    @Override
    public Skill fetchSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    @Override
    public Skill update(Skill s) {
        return this.skillRepository.save(s);
    }

    @Override
    public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());

        return rs;
    }

    @Override
    public void deleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete subscriber (inside subscriber_skill table)
        // currentSkill.getSubscribers().forEach(subs ->
        // subs.getSkills().remove(currentSkill));

        // delete skill
        this.skillRepository.delete(currentSkill);
    }

}
