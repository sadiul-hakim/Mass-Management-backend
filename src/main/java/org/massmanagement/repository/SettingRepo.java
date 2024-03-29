package org.massmanagement.repository;

import org.massmanagement.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepo extends JpaRepository<Setting,Long> {
    Optional<Setting> findByName(String name);
}
