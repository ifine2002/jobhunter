package vn.ifine.jobhunter.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Permission;
import vn.ifine.jobhunter.domain.Role;
import vn.ifine.jobhunter.domain.User;
import vn.ifine.jobhunter.repository.PermissionRepository;
import vn.ifine.jobhunter.repository.RoleRepository;
import vn.ifine.jobhunter.repository.UserRepository;
import vn.ifine.jobhunter.util.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            // api manage company
            arr.add(new Permission("Create a company", "/api/admin/companies", "POST", "COMPANIES"));
            arr.add(new Permission("Update a company", "/api/admin/companies", "PUT", "COMPANIES"));
            arr.add(new Permission("Delete a company", "/api/admin/companies/{id}", "DELETE", "COMPANIES"));
            arr.add(new Permission("Get a company by id", "/api/admin/companies/{id}", "GET", "COMPANIES"));
            arr.add(new Permission("Get companies with pagination", "/api/admin/companies", "GET", "COMPANIES"));

            arr.add(new Permission("Create a job", "/api/admin/jobs", "POST", "JOBS"));
            arr.add(new Permission("Update a job", "/api/admin/jobs", "PUT", "JOBS"));
            arr.add(new Permission("Delete a job", "/api/admin/jobs/{id}", "DELETE", "JOBS"));
            arr.add(new Permission("Get a job by id", "/api/admin/jobs/{id}", "GET", "JOBS"));
            arr.add(new Permission("Get jobs with pagination", "/api/admin/jobs", "GET", "JOBS"));

            arr.add(new Permission("Create a permission", "/api/admin/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/admin/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/admin/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by id", "/api/admin/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get permissions with pagination", "/api/admin/permissions", "GET", "PERMISSIONS"));

            arr.add(new Permission("Create a resume", "/api/admin/resumes", "POST", "RESUMES"));
            arr.add(new Permission("Update a resume", "/api/admin/resumes", "PUT", "RESUMES"));
            arr.add(new Permission("Delete a resume", "/api/admin/resumes/{id}", "DELETE", "RESUMES"));
            arr.add(new Permission("Get a resume by id", "/api/admin/resumes/{id}", "GET", "RESUMES"));
            arr.add(new Permission("Get resumes with pagination", "/api/admin/resumes", "GET", "RESUMES"));

            arr.add(new Permission("Create a role", "/api/admin/roles", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/api/admin/roles", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/api/admin/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("Get a role by id", "/api/admin/roles/{id}", "GET", "ROLES"));
            arr.add(new Permission("Get roles with pagination", "/api/admin/roles", "GET", "ROLES"));

            arr.add(new Permission("Create a user", "/api/admin/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/api/admin/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/api/admin/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/api/admin/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get users with pagination", "/api/admin/users", "GET", "USERS"));

            arr.add(new Permission("Create a subscriber", "/api/admin/subscribers", "POST", "SUBSCRIBERS"));
            arr.add(new Permission("Update a subscriber", "/api/admin/subscribers", "PUT", "SUBSCRIBERS"));
            arr.add(new Permission("Delete a subscriber", "/api/admin/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
            arr.add(new Permission("Get a subscriber by id", "/api/admin/subscribers/{id}", "GET", "SUBSCRIBERS"));
            arr.add(new Permission("Get subscribers with pagination", "/api/admin/subscribers", "GET", "SUBSCRIBERS"));

            arr.add(new Permission("Create a skill", "/api/admin/skills", "POST", "SKILLS"));
            arr.add(new Permission("Update a skill", "/api/admin/skills", "PUT", "SKILLS"));
            arr.add(new Permission("Delete a skill", "/api/admin/skills/{id}", "DELETE", "SKILLS"));
            arr.add(new Permission("Get skill with pagination", "/api/admin/skills", "GET", "SKILLS"));

            // arr.add(new Permission("Download a file", "/api/admin/files", "POST",
            // "FILES"));
            // arr.add(new Permission("Upload a file", "/api/admin/files", "GET", "FILES"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("hn");
            adminUser.setAge(25);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }

}
