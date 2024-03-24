package org.massmanagement.model.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.massmanagement.model.User;
import org.massmanagement.repository.UserRoleRepo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserDeserializer extends JsonDeserializer<User> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String STATUS = "status";
    private final UserRoleRepo userRoleRepo;
    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        User user = new User();

        JsonNode node = jsonParser.readValueAsTree();
        long id = node.get(ID).asLong();
        String name = node.get(NAME).asText();
        String phone = node.get(PHONE).asText();
        String email = node.get(EMAIL).asText();
        String address = node.get(ADDRESS).asText();
        String password = node.get(PASSWORD).asText();

        long roleId = node.get(ROLE).asLong();
        var role = userRoleRepo.findById(roleId).orElse(null);

        long status = node.get(STATUS).asLong();

        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);
        user.setPassword(password);
        user.setRole(role);
        user.setStatus(status);

        return user;
    }
}
