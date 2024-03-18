package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.Period;
import org.massmanagement.repository.PeriodRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeriodService {
    private final PeriodRepo periodRepo;
    public Period save(Period period) {
        log.info("Saving period : {}", period);
        return periodRepo.save(period);
    }

    public Period getById(long id) {
        log.info("Getting period by id : {}", id);
        return periodRepo.findById(id).orElse(new Period());
    }

    public List<Period> getAll() {
        log.info("Getting all periods.");
        return periodRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting period id : {}", id);
        try {
            periodRepo.deleteById(id);
            log.info("Period {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
