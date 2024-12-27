package vn.ifine.jobhunter.controller.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ifine.jobhunter.domain.Subscriber;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.service.SubscriberService;
import vn.ifine.jobhunter.util.SecurityUtil;
import vn.ifine.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/auth")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    public ResponseEntity<ApiResponse<Subscriber>> create(@Valid @RequestBody Subscriber sub)
            throws IdInvalidException {
        // check email
        boolean isExist = this.subscriberService.isExistsByEmail(sub.getEmail());
        if (isExist == true) {
            throw new IdInvalidException("Email " + sub.getEmail() + " đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create a subscriber", this.subscriberService.create(sub)));
    }

    @PutMapping("/subscribers")
    public ResponseEntity<ApiResponse<Subscriber>> update(@RequestBody Subscriber subsRequest)
            throws IdInvalidException {
        // check id
        Subscriber subsDB = this.subscriberService.findById(subsRequest.getId());
        if (subsDB == null) {
            throw new IdInvalidException("Id " + subsRequest.getId() + " không tồn tại");
        }
        return ResponseEntity.ok()
                .body(ApiResponse.success("Update a subscriber", this.subscriberService.update(subsDB, subsRequest)));
    }

    @PostMapping("/subscribers/skills")
    public ResponseEntity<ApiResponse<Subscriber>> getSubscribersSkill() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok()
                .body(ApiResponse.success("Get subscriber's skill", this.subscriberService.findByEmail(email)));
    }
}
