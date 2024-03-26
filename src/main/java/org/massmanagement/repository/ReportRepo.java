package org.massmanagement.repository;

import org.massmanagement.model.ReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<ReportModel,Long> {
}
