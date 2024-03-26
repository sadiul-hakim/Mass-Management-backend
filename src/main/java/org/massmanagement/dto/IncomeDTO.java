package org.massmanagement.dto;

import org.massmanagement.model.TransactionType;


public record IncomeDTO(long id, TransactionType type,
                        UserDTO user, long amount, String date) {
}
