package vn.ifine.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCreateResumeDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
