package com.example.workflow.delegates;

import com.example.workflow.exception.NotFoundRedirectException;
import com.example.workflow.model.Redirect;
import com.example.workflow.repository.RedirectRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;


@Named
@Component
@Slf4j
public class RedirectRemover implements JavaDelegate {


    private RedirectRepository redirectRepository;

    public RedirectRemover(RedirectRepository redirectRepository) {
        this.redirectRepository = redirectRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int count = 0;
        for (Redirect redirect: redirectRepository.findAll()){
            if ((Timestamp.from(Instant.now()).getTime() - redirect.getTime().getTime()) / 1000 > 300){
                removeRedirect(redirect);
                count++;
            }
        }
        if (count == 0)
            log.info("Scheduled redirects cleaning task: Not found unused redirects");
        else
            log.info("Scheduled redirects cleaning task: Successfully cleaned {} unused redirects", count);
    }

    public void removeRedirect(Redirect redirect) throws NotFoundRedirectException {
        redirectRepository.delete(redirect);
    }
}
