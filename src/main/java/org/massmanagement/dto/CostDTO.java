package org.massmanagement.dto;

import org.massmanagement.model.TransactionType;


public record CostDTO(long id, TransactionType type,
                      long amount, String date) {
}
