package org.massmanagement.dto;

import java.util.ArrayList;
import java.util.List;

public record MailStructure(String subject, String mailText, List<String> toMails) {
    public MailStructure() {
        this("", "", new ArrayList<>());
    }
}
