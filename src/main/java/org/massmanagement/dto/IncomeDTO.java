package org.massmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.massmanagement.model.TransactionType;
import org.massmanagement.model.User;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDTO {
    private long id;
    private TransactionType type;
    private User user;
    private long amount;
    private Timestamp date;
}
