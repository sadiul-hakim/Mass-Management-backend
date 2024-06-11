package org.massmanagement.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.MailStructure;
import org.massmanagement.model.Setting;
import org.massmanagement.model.User;
import org.massmanagement.util.SettingParameter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final MailService mailService;
    private final SettingService settingService;
    private final UserService userService;

    public void sendAlertMessage(HttpServletRequest request) {

        final String user = request.getRemoteUser();
        final String address = request.getRemoteAddr();
        final int port = request.getRemotePort();

        Thread.ofVirtual().start(() -> {
            try {
                StringBuilder message = new StringBuilder();
                Setting setting = settingService.getByName(SettingParameter.ENTRY_NAME);
                long managerRoleId = setting.getProperty(SettingParameter.USER_ROLE_MANGER);
                Optional<User> manager = userService.getAllByRole(managerRoleId).stream().findFirst();

                if (manager.isEmpty())
                    return;

                message.append(user)
                        .append(" successfully logged in from ")
                        .append(address)
                        .append(":")
                        .append(port)
                        .append(" address");

                MailStructure structure = new MailStructure("Login Alert", message.toString());
                mailService.send(manager.get().getEmail(), structure);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        });
    }
}
