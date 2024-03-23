package org.massmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.massmanagement.model.TransactionType;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostDTO {
    private long id;
    private TransactionType type;
    private long amount;
    private String date;
}
