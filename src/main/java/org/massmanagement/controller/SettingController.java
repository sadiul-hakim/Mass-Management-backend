package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Setting;
import org.massmanagement.service.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/setting/v1")
@RequiredArgsConstructor
public class SettingController {
    private final SettingService settingService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Setting setting){
        Setting save = settingService.save(setting);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/get-by-name")
    public ResponseEntity<?> getByName(@RequestParam String name){
        Setting byName = settingService.getByName(name);

        return byName != null ? ResponseEntity.ok(byName):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error","Could not get setting."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProperties(@RequestParam String name){
        boolean deleted = settingService.deleteSetting(name);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message","Properties deleted successfully.")):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error","Could not delete properties."));
    }
}
