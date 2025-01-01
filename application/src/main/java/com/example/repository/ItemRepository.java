package com.example.repository;

import com.example.model.entity.Item;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Item} entities.
 * Provides methods to query items, their count, and handle favorite items.
 */
@RepositoryDefinition(domainClass = Item.class, idClass = Integer.class)
public interface ItemRepository {

    /**
     * Finds a list of items based on name, category, and pagination.
     *
     * @param name       the name of the item (can be partial match)
     * @param category   the category of the item
     * @param pageSize   the number of items per page
     * @param pageNumber the page number for pagination
     * @return a list of items that match the filters
     */
    @Query("select id, item.name, item.properties from item " +
            "left join item_category ic on item.id = ic.item_id " +
            "where (:name is NULL OR lower(item.name) LIKE '%' || lower(:name) || '%') " +
            "and (:category is NULL OR ic.category = :category) " +
            "limit :page_size offset :page_number * :page_size")
    List<Item> findFilteredItems(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("page_size") long pageSize,
                                 @Param("page_number") long pageNumber);

    /**
     * Counts the number of items that match the provided name and category filters.
     *
     * @param name     the name of the item (can be partial match)
     * @param category the category of the item
     * @return the count of matching items
     */
    @Query("select count(*) from item " +
            "left join item_category ic on item.id = ic.item_id " +
            "where (:name is NULL OR lower(item.name) LIKE '%' || lower(:name) || '%') " +
            "and (:category is NULL OR ic.category = :category)")
    Long countFilteredItems(@Param("name") String name,
                            @Param("category") String category);

    /**
     * Finds an item by its ID.
     *
     * @param id the ID of the item
     * @return an Optional containing the item if found
     */
    @Query("select * from item where id = :id")
    Optional<Item> findItem(@Param("id") long id);

    /**
     * Finds a list of favorite items for a user, filtered by name and category.
     *
     * @param user       the login of the user
     * @param name       the name of the item (can be partial match)
     * @param category   the category of the item
     * @param pageSize   the number of items per page
     * @param pageNumber the page number for pagination
     * @return a list of favorite items
     */
    @Query("select item.id, item.name, item.properties from item " +
            "join favourite f on item.id = f.item_id " +
            "left join item_category ic on item.id = ic.item_id " +
            "where user_login = :user " +
            "and (:name is NULL OR lower(item.name) LIKE '%' || lower(:name) || '%') " +
            "and (:category is NULL OR ic.category = :category) " +
            "limit :page_size offset :page_number * :page_size")
    List<Item> findFavouriteItems(@Param("user") String user,
                                  @Param("name") String name,
                                  @Param("category") String category,
                                  @Param("page_size") long pageSize,
                                  @Param("page_number") long pageNumber);

    /**
     * Counts the number of favorite items for a user, filtered by name and category.
     *
     * @param user     the login of the user
     * @param name     the name of the item (can be partial match)
     * @param category the category of the item
     * @return the count of favorite items
     */
    @Query("select count(*) from item " +
            "join favourite f on item.id = f.item_id " +
            "left join item_category ic on item.id = ic.item_id " +
            "where user_login = :user " +
            "and (:name is NULL OR lower(item.name) LIKE '%' || lower(:name) || '%') " +
            "and (:category is NULL OR ic.category = :category)")
    Long countFavouriteItems(@Param("user") String user,
                             @Param("name") String name,
                             @Param("category") String category);

    /**
     * Checks if an item is marked as a favorite by the user.
     *
     * @param user the login of the user
     * @param item the ID of the item
     * @return true if the item is a favorite, false otherwise
     */
    @Query("select exists(select * from favourite where user_login = :user and item_id = :item)")
    Boolean isFavourite(@Param("user") String user,
                        @Param("item") long item);

    /**
     * Adds an item to the user's list of favorites.
     *
     * @param user the login of the user
     * @param item the ID of the item to add to favorites
     */
    @Modifying
    @Query("insert into favourite(user_login, item_id) values (:user, :item)")
    void addFavouriteItem(@Param("user") String user,
                          @Param("item") long item);

    /**
     * Removes an item from the user's list of favorites.
     *
     * @param user the login of the user
     * @param item the ID of the item to remove from favorites
     */
    @Modifying
    @Query("delete from favourite where user_login = :user and item_id = :item")
    void deleteFavouriteItem(@Param("user") String user,
                             @Param("item") long item);

    /**
     * Checks if an item exists by its ID.
     *
     * @param item the ID of the item
     * @return true if the item exists, false otherwise
     */
    @Query("select exists(select * from item where id = :item)")
    Boolean isItemExists(@Param("item") long item);

    /**
     * Retrieves the self-price of an item by its ID.
     *
     * @param id the ID of the item
     * @return an Optional containing the self-price of the item
     */
    @Query("select cost::integer from calculate_selfprice(:id::integer) as cost")
    Optional<Long> getSelfprice(@Param("id") long id);

    /**
     * Retrieves a list of distinct categories of items.
     *
     * @return a list of item categories
     */
    @Query("select distinct category from item_category")
    List<String> getCategories();

    /**
     * Retrieves a list of distinct categories of items marked as favorites by a user.
     *
     * @param username the login of the user
     * @return a list of favorite item categories
     */
    @Query("select distinct category from item_category " +
            "join favourite f on item_category.item_id = f.item_id " +
            "where user_login = :user")
    List<String> getFavouritesCategories(@Param("user") String username);

}
