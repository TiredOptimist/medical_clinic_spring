package ru.medicalclinic.medical_clinic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionService {
    private final PlatformTransactionManager transactionManager;
    private TransactionStatus transactionStatus;

    public void saveCheckpoint() {
        if (transactionStatus == null || transactionStatus.isCompleted()) {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setName("SavepointTransaction");
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            transactionStatus = transactionManager.getTransaction(def);
            log.info("Checkpoint saved.");
        } else {
            log.warn("Checkpoint already exists. Skipping save.");
        }
    }

    public void commitCheckpoint() {
        if (transactionStatus != null && !transactionStatus.isCompleted()) {
            transactionManager.commit(transactionStatus);
            transactionStatus = null;
            log.info("Checkpoint committed.");
        } else {
            log.warn("No active checkpoint to commit.");
        }
    }

    public void performRollbackToLastCheckpoint() {
        if (transactionStatus != null && !transactionStatus.isCompleted()) {
            transactionManager.rollback(transactionStatus);
            transactionStatus = null;
            log.info("Rolled back to last checkpoint.");
        } else {
            log.warn("No active checkpoint to rollback to.");
        }
    }
}