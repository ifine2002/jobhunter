package vn.ifine.jobhunter.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.ifine.jobhunter.domain.response.ApiResponse;
import vn.ifine.jobhunter.service.SubscriberService;

@RestController
@RequestMapping("/api/public")
public class EmailController {
    private final SubscriberService subscriberService;

    public EmailController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @Scheduled(cron = "0 0 21 ? * 6")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("boyyhp1@gmail.com", "test send email",
        // "<h1><b> Hello </b></h1>", false,
        // true);
        // this.emailService.sendEmailFromTemplateSync("boyyhp1@gmail.com", "test send
        // email", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return ResponseEntity.ok().body(ApiResponse.success("Send email successfully", null));
    }
}
