package vn.ifine.jobhunter.controller.client;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.ifine.jobhunter.domain.Skill;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.domain.response.ResultPaginationDTO;
import vn.ifine.jobhunter.service.SkillService;

@RestController
@RequestMapping("/api/public")
public class SkillController {
    private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAll(@Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Fetch all skills", this.skillService.fetchAllSkills(spec, pageable)));
    }

}
