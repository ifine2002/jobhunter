package vn.ifine.jobhunter.domain.response.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.ifine.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private CompanyUser company;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;

    }
}
