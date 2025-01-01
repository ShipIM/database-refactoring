package com.example.repository;

import com.example.model.entity.ItemsForPeriod;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing {@link ItemsForPeriod} entities.
 * Provides methods to query items for a specified period and their count.
 */
@RepositoryDefinition(domainClass = ItemsForPeriod.class, idClass = Integer.class)
public interface ItemsForPeriodRepository {

    /**
     * Retrieves a list of items for a specified period with the maximum cost to buy per day.
     *
     * @param start      the start date of the period
     * @param end        the end date of the period
     * @param id         the ID associated with the period
     * @param pageSize   the number of items to retrieve per page
     * @param pageNumber the page number for pagination
     * @return a list of items for the specified period
     */
    @Query("select * from get_max_cost_buy_per_day_for_period(:start, :end, :id::integer)" +
            " limit :page_size offset :page_number * :page_size")
    List<ItemsForPeriod> getItemsForPeriod(@Param("start") LocalDate start,
                                           @Param("end") LocalDate end,
                                           @Param("id") long id,
                                           @Param("page_size") long pageSize,
                                           @Param("page_number") long pageNumber);

    /**
     * Retrieves the total count of items for a specified period.
     *
     * @param start the start date of the period
     * @param end   the end date of the period
     * @param id    the ID associated with the period
     * @return the total count of items for the specified period
     */
    @Query("select count(*) from get_max_cost_buy_per_day_for_period(:start, :end, :id::integer)")
    Long countItemsForPeriod(@Param("start") LocalDate start,
                             @Param("end") LocalDate end,
                             @Param("id") long id);

}
