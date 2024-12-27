package vn.ifine.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.ifine.jobhunter.domain.Job;
import vn.ifine.jobhunter.domain.Subscriber;
import vn.ifine.jobhunter.domain.response.email.ResEmailJob;

@Service
public interface SubscriberService {
    boolean isExistsByEmail(String email);

    Subscriber create(Subscriber subs);

    Subscriber update(Subscriber subsDB, Subscriber subsRequest);

    Subscriber findById(long id);

    ResEmailJob convertJobToSendEmail(Job job);

    void sendSubscribersEmailJobs();

    Subscriber findByEmail(String email);
}
