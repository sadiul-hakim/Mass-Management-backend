package org.massmanagement.service;

import org.massmanagement.model.Rule;
import org.massmanagement.repository.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RuleService {
    private final RuleRepository ruleRepository;

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Rule save(Rule rule) {
        log.info("Saving rule {}", rule);

        if(rule.getRule().isEmpty()){
            log.info("Can not save empty rule!");
            return new Rule();
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
