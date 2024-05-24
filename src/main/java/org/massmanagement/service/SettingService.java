package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.Setting;
import org.massmanagement.repository.SettingRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepo settingRepo;
    public Setting save(Setting setting){
        try{
            log.info("Saving setting.");

            Setting existingSetting = getByName(setting.getName());
            if(existingSetting == null){
                log.error("Setting does not exists.");
                return settingRepo.save(setting);
            }

            existingSetting.setProperties(setting.getProperties());
            existingSetting.setExcludeTransactionTypes(setting.getExcludeTransactionTypes());
            existingSetting.setBillTypes(setting.getBillTypes());
            return settingRepo.save(existingSetting);
        }catch (Exception ex){
            log.error("Error occurred while saving setting. Cause {}",ex.getMessage());
            return null;
        }
    }

    public Setting getByName(String name){
        log.info("Getting setting by name {}",name);
        return settingRepo.findByName(name).orElse(null);
    }

    public boolean deleteSetting(String name){
        try{
            log.info("Deleting setting {}",name);

            Setting setting = getByName(name);
            if(setting == null){
                log.info("Setting does not exists.");
                return false;
            }

            setting.setProperties(new HashMap<>());
            setting.setExcludeTransactionTypes(new ArrayList<>());
            setting.setBillTypes(new ArrayList<>());

            var saved = save(setting);
            return saved.getProperties().isEmpty();
        }catch (Exception ex){
            log.error("Error occurred while deleting setting : {}",ex.getMessage());
            return false;
        }
    }

    public boolean isInvalid(Setting setting){
        if(setting == null || setting.getProperties().isEmpty() || setting.getExcludeTransactionTypes().isEmpty())
            return false;

        return setting.getProperties().values().stream().anyMatch(value -> value == 0);
    }
}
