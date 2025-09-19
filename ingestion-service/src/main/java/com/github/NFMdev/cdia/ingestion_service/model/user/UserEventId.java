package com.github.NFMdev.cdia.ingestion_service.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventId implements Serializable {
    private Long user;
    private Long event;
}
