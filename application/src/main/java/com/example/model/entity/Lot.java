package com.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a lot with details such as ID, login, current price, buy price, and end time.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lot {

    /**
     * The unique ID of the lot.
     */
    private long id;

    /**
     * The login associated with the lot.
     */
    private String login;

    /**
     * The current price of the lot.
     */
    private long current;

    /**
     * The buy price of the lot.
     */
    private long buy;

    /**
     * The end time of the lot.
     */
    private LocalDateTime end;

}
