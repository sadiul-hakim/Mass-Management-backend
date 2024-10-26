package org.massmanagement;

import lombok.RequiredArgsConstructor;
import org.massmanagement.dto.RoleDTO;
import org.massmanagement.model.Setting;
import org.massmanagement.model.User;
import org.massmanagement.model.UserRole;
import org.massmanagement.model.UserStatus;
import org.massmanagement.service.SettingService;
import org.massmanagement.service.UserRoleService;
import org.massmanagement.service.UserService;
import org.massmanagement.service.UserStatusService;
import org.massmanagement.util.SettingParameter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
@RequiredArgsConstructor
public class MassManagementApplication implements CommandLineRunner {
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final SettingService settingService;

    public static void main(String[] args) {
        SpringApplication.run(MassManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        UserStatus activeStatus = userStatusService.getByStatus("Active");
        if (activeStatus.getId() == 0) {
            activeStatus = userStatusService.save(new UserStatus(0, "Active", "Active User"));
        }

        UserRole adminRole = userRoleService.getModelByRole("ROLE_MANAGER");
        if (adminRole.getId() == 0) {
            RoleDTO role = userRoleService.save(new UserRole(0, "ROLE_MANAGER", "", new ArrayList<>()));
            adminRole = userRoleService.getModelByRole(role.role());
        }

        if (userService.getByRole(adminRole.getId()).isEmpty()) {
            User user = new User(0, "Sadiul Hakim", "01304802986", "sadiulhakim@gmail.com",
                    "hakim@123", "Kushtia", adminRole, activeStatus.getId(),
                    new Timestamp(System.currentTimeMillis()));
            userService.save(user);
        }

        if (settingService.getByName(SettingParameter.ENTRY_NAME).getId() == 0) {
            settingService.save(new Setting(0, SettingParameter.ENTRY_NAME, new HashMap<>(), new ArrayList<>(), new ArrayList<>()));
        }
    }
}
