package vn.ifine.jobhunter.controller.admin;

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
import vn.ifine.jobhunter.domain.Skill;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.service.SkillService;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/admin")
public class SkillManagementController {
    private SkillService skillService;

    public SkillManagementController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> create(@Valid @RequestBody Skill s) throws IdInvalidException {
        // check name
        if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
            throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a skill", this.skillService.createSkill(s)));
    }

    @PutMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> update(@Valid @RequestBody Skill s) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.fetchSkillById(s.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + s.getId() + " không tồn tại");
        }
        // check name
        if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
            throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại");
        }

        // update skill
        currentSkill.setName(s.getName());
        return ResponseEntity.ok().body(ApiResponse.success("Update a skill", this.skillService.update(currentSkill)));
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAll(@Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Fetch all skills", this.skillService.fetchAllSkills(spec, pageable)));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + id + " không tồi tại");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(ApiResponse.success("Delete a skill", null));
    }
}
