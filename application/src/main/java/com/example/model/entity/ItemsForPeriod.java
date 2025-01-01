package com.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents the details of items for a specific period, including the day, maximum cost, and quantity.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsForPeriod {

    /**
     * The day of the period for the item.
     */
    private LocalDate day;

    /**
     * The maximum cost of the item during the period.
     */
    private Long maxCostBuy;

    /**
     * The quantity of the item for the given period.
     */
    private Long quantity;

}
