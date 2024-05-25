package org.massmanagement.repository;

import org.massmanagement.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule,Long> {
}
