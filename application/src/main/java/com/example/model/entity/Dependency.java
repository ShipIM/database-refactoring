package com.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a dependency with a name, ID, and level.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Dependency {

    /**
     * The name of the dependency.
     */
    private String name;

    /**
     * The unique ID of the dependency.
     */
    private Long id;

    /**
     * The level of the dependency, indicating its position or priority.
     */
    private Long level;

}
