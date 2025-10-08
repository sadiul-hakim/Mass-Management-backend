package org.massmanagement.controller;

import org.massmanagement.model.Setting;
import org.massmanagement.service.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/setting/v1")
class SettingController {
    private final SettingService settingService;

    SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody Setting setting) {
        Setting save = settingService.save(setting);
        return ResponseEntity.ok(save);
    }

    @GetMapping(value = "/get-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByName(@RequestParam String name) {
        Setting byName = settingService.getByName(name);

        return byName != null ? ResponseEntity.ok(byName) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not get setting."));
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProperties(@RequestParam String name) {
        boolean deleted = settingService.deleteSetting(name);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Properties deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete properties."));
    }
}
