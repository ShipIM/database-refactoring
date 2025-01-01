package com.example.repository;

import com.example.model.entity.Lot;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing {@link Lot} entities.
 * Provides methods to query active lots for a specific item and their count.
 */
@RepositoryDefinition(domainClass = Lot.class, idClass = Long.class)
public interface LotRepository {

    /**
     * Retrieves a list of active lots for a specific item with pagination.
     *
     * @param item       the ID of the item for which the active lots are retrieved
     * @param pageSize   the number of lots per page
     * @param pageNumber the page number for pagination
     * @return a list of active lots for the specified item
     */
    @Query("select lot.id, lot.user_login as login, cost_current as current, cost_buy as buy, time_end as \"end\" from lot " +
            "join lot_cost_information lci on lot.id = lci.lot_id " +
            "join lot_status_information lsi on lot.id = lsi.lot_id " +
            "join lot_time_information lti on lot.id = lti.lot_id " +
            "where item_id = :item " +
            "and status = 'ACTIVE' " +
            "limit :page_size offset :page_number * :page_size")
    List<Lot> findActiveLots(@Param("item") long item,
                             @Param("page_size") long pageSize,
                             @Param("page_number") long pageNumber);

    /**
     * Retrieves the total count of active lots for a specific item.
     *
     * @param item the ID of the item for which the active lot count is retrieved
     * @return the total count of active lots for the specified item
     */
    @Query("select count(*) from lot " +
            "join lot_status_information lsi on lot.id = lsi.lot_id " +
            "where item_id = :item " +
            "and status = 'ACTIVE'")
    Long countActiveLots(@Param("item") long item);

}
