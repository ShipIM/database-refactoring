package com.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an item with an ID, name, and properties.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    /**
     * The unique ID of the item.
     */
    private Integer id;

    /**
     * The name of the item.
     */
    private String name;

    /**
     * Additional properties or details related to the item.
     */
    private String properties;

}
