package org.massmanagement.service;

import org.massmanagement.model.Setting;
import org.massmanagement.repository.SettingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SettingService {
    private final SettingRepo settingRepo;

    private static final Logger log = LoggerFactory.getLogger(SettingService.class);

    public SettingService(SettingRepo settingRepo) {
        this.settingRepo = settingRepo;
    }

    public Setting save(Setting setting) {
        try {
            log.info("Saving setting.");

            Setting existingSetting = getByName(setting.getName());
            if (existingSetting == null || existingSetting.getId() == 0) {
                log.error("Setting does not exists.");
                return settingRepo.save(setting);
            }

            existingSetting.setProperties(setting.getProperties());
            existingSetting.setExcludeTransactionTypes(setting.getExcludeTransactionTypes());
            existingSetting.setBillTypes(setting.getBillTypes());
            return settingRepo.save(existingSetting);
        } catch (Exception ex) {
            log.error("Error occurred while saving setting. Cause {}", ex.getMessage());
            return new Setting();
        }
    }

    public Setting getByName(String name) {
        log.info("Getting setting by name {}", name);
        return settingRepo.findByName(name).orElse(new Setting());
    }

    public boolean deleteSetting(String name) {
        try {
            log.info("Deleting setting {}", name);

            Setting setting = getByName(name);
            if (setting == null) {
                log.info("Setting does not exists.");
                return false;
            }

            setting.setProperties(new HashMap<>());
            setting.setExcludeTransactionTypes(new ArrayList<>());
            setting.setBillTypes(new ArrayList<>());

            var saved = save(setting);
            return saved.getProperties().isEmpty();
        } catch (Exception ex) {
            log.error("Error occurred while deleting setting : {}", ex.getMessage());
            return false;
        }
    }

    public boolean isInvalid(Setting setting) {
        if (setting == null || setting.getProperties().isEmpty())
            return false;

        return setting.getProperties().values().stream()
                .anyMatch(value -> {
                    if (value == null) return true;

                    return switch (value) {
                        case Long l -> l == 0L;
                        case Integer i -> i == 0;
                        case Double d -> d == 0.0;
                        case Float f -> f == 0.0f;
                        case String s -> s.isEmpty();
                        default -> false;
                    };
                });
    }
}
