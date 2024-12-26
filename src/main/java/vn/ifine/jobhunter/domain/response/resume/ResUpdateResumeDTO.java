package vn.ifine.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateResumeDTO {
    private Instant updatedAt;
    private String updatedBy;
}
