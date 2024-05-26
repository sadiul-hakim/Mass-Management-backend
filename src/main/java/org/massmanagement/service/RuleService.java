package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.Rule;
import org.massmanagement.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;

    public Rule save(Rule rule) {
        log.info("Saving rule {}", rule);

        if(rule.getRule().isEmpty()){
            log.info("Can not save empty rule!");
            return null;
        }
        rule.setDate(new Timestamp(System.currentTimeMillis()));
        return ruleRepository.save(rule);
    }

    public List<Rule> getAll() {
        log.info("Getting all rules.");
        return ruleRepository.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting rule {}", id);
        ruleRepository.deleteById(id);
        return true;
    }
}
