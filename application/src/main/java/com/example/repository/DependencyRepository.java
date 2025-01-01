package com.example.repository;

import com.example.model.entity.Dependency;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing {@link Dependency} entities.
 * Provides methods to query dependencies and their count using a custom SQL function.
 */
@RepositoryDefinition(domainClass = Dependency.class, idClass = Integer.class)
public interface DependencyRepository {

    /**
     * Retrieves a list of dependencies based on the provided ID, page size, and page number.
     *
     * @param id         the ID to filter dependencies by
     * @param pageSize   the number of dependencies to retrieve per page
     * @param pageNumber the page number for pagination
     * @return a list of dependencies
     */
    @Query("select * from dependency_parser(:id::integer)" +
            " limit :page_size offset :page_number * :page_size")
    List<Dependency> getDependencies(@Param("id") long id,
                                     @Param("page_size") long pageSize,
                                     @Param("page_number") long pageNumber);

    /**
     * Retrieves the total count of dependencies for the provided ID.
     *
     * @param id the ID to filter dependencies by
     * @return the total count of dependencies
     */
    @Query("select count(*) from dependency_parser(:id::integer)")
    Long getDependenciesCount(@Param("id") long id);

}
