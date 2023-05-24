package com.example.workflow.delegates;

import com.example.workflow.repository.WithdrawRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;


@Component
@Named
@Slf4j
public class WithdrawRequestRemover implements JavaDelegate {

    private WithdrawRepository withdrawRepository;

    private final long ONE_HOUR_IN_MILLIS = 3600000;

    public WithdrawRequestRemover(WithdrawRepository withdrawRepository) {
        this.withdrawRepository = withdrawRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int rawsDeleted = withdrawRepository.deletePendingWithdraws(new Timestamp(Timestamp.from(Instant.now()).getTime() - ONE_HOUR_IN_MILLIS));
        log.info("Delete old pending withdraw requests. Rows deleted = "+rawsDeleted);
    }
}
