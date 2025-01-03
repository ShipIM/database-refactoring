package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.metrics.DatabaseQueriesTotal;
import com.example.metrics.DatabaseQueryDuration;
import com.example.model.entity.Dependency;
import com.example.model.entity.Item;
import com.example.model.entity.ItemsForPeriod;
import com.example.model.entity.Lot;
import com.example.repository.DependencyRepository;
import com.example.repository.ItemRepository;
import com.example.repository.ItemsForPeriodRepository;
import com.example.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class responsible for managing items, lots, dependencies, and items for a period.
 * Provides methods to retrieve, add, remove, and check items in various contexts (favourites, dependencies, etc.).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final LotRepository lotRepository;
    private final ItemsForPeriodRepository itemsForPeriodRepository;
    private final DependencyRepository dependencyRepository;
    private final DetailsService detailsService;

    private final DatabaseQueriesTotal dbQueriesTotal;
    private final DatabaseQueryDuration dbQueryDuration;

    /**
     * Retrieves a filtered list of items based on the provided name and category.
     *
     * @param name     the name filter for items
     * @param category the category filter for items
     * @param pageable pagination details
     * @return a pair containing the list of items and the total count
     */
    public Pair<List<Item>, Long> getItems(String name, String category, Pageable pageable) {
        dbQueriesTotal.increment();

        var total = dbQueryDuration.record(() -> itemRepository.countFilteredItems(name, category));
        var items = dbQueryDuration.record(() ->
                itemRepository.findFilteredItems(name, category, pageable.getPageSize(), pageable.getPageNumber())
        );

        log.info("Get items");

        return Pair.of(items, total);
    }

    /**
     * Retrieves a specific item by its ID.
     *
     * @param id the ID of the item to retrieve
     * @return the item with the specified ID
     * @throws EntityNotFoundException if no item with the given ID is found
     */
    public Item getItem(long id) {
        dbQueriesTotal.increment();

        log.info("Get item with id {}", id);

        return dbQueryDuration.record(() ->
                itemRepository.findItem(id)
                        .orElseThrow(() -> new EntityNotFoundException("There is no item with such an identifier"))
        );
    }

    /**
     * Retrieves a list of favourite items for a user, with optional name and category filters.
     *
     * @param email    the user's email address
     * @param name     the name filter for favourite items
     * @param category the category filter for favourite items
     * @param pageable pagination details
     * @return a pair containing the list of favourite items and the total count
     * @throws EntityNotFoundException if the user does not exist
     */
    public Pair<List<Item>, Long> getFavouriteItems(String email, String name, String category, Pageable pageable) {
        if (!detailsService.isUserExists(email)) {
            throw new EntityNotFoundException("There is no user with this ID");
        }

        dbQueriesTotal.increment();

        var total = dbQueryDuration.record(() ->
                itemRepository.countFavouriteItems(email, name, category)
        );
        var items = dbQueryDuration.record(() ->
                itemRepository.findFavouriteItems(email, name, category, pageable.getPageSize(), pageable.getPageNumber())
        );

        log.info("Get favourite item user {}", email);

        return Pair.of(items, total);
    }

    /**
     * Checks if an item is marked as a favourite by a user.
     *
     * @param email the user's email address
     * @param id    the ID of the item to check
     * @return true if the item is a favourite, false otherwise
     * @throws EntityNotFoundException if the user or item does not exist
     */
    public boolean isFavourite(String email, long id) {
        if (!detailsService.isUserExists(email)) {
            throw new EntityNotFoundException("There is no user with this ID");
        }
        if (!this.isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        log.info("Is item with id {} favourite to user {}", id, email);

        return dbQueryDuration.record(() ->
                itemRepository.isFavourite(email, id)
        );
    }

    /**
     * Adds an item to the user's list of favourites.
     *
     * @param username the user's email address
     * @param id       the ID of the item to add as a favourite
     * @throws EntityNotFoundException if the user or item does not exist
     */
    public void addFavouriteItem(String username, long id) {
        if (!detailsService.isUserExists(username)) {
            throw new EntityNotFoundException("There is no user with this ID");
        }
        if (!this.isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        dbQueryDuration.record(() ->
                itemRepository.addFavouriteItem(username, id)
        );
        log.info("Add item with id {} to favourite to user {}", id, username);

    }

    /**
     * Removes an item from the user's list of favourites.
     *
     * @param username the user's email address
     * @param id       the ID of the item to remove from favourites
     * @throws EntityNotFoundException if the user or item does not exist
     */
    public void deleteFavouriteItem(String username, long id) {
        if (!detailsService.isUserExists(username)) {
            throw new EntityNotFoundException("There is no user with this ID");
        }
        if (!this.isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        dbQueryDuration.record(() ->
                itemRepository.deleteFavouriteItem(username, id)
        );

        log.info("Delete item with id {} from favourite to user {}", id, username);
    }

    /**
     * Checks if an item exists by its ID.
     *
     * @param id the ID of the item to check
     * @return true if the item exists, false otherwise
     */
    public boolean isItemExists(long id) {
        dbQueriesTotal.increment();

        return dbQueryDuration.record(() ->
                itemRepository.isItemExists(id)
        );
    }

    /**
     * Retrieves the self-price of an item.
     *
     * @param id the ID of the item
     * @return the self-price of the item
     * @throws EntityNotFoundException if the item does not exist or the self-price cannot be calculated
     */
    public long getSelfPrice(long id) {
        if (!isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        log.info("Get selfprice to item with id {}", id);

        return dbQueryDuration.record(() ->
                itemRepository.getSelfprice(id)
                        .orElseThrow(() -> new EntityNotFoundException("It is impossible to calculate the self price"))
        );
    }

    /**
     * Retrieves a list of items for a given period.
     *
     * @param start    the start date of the period
     * @param end      the end date of the period
     * @param id       the ID of the item
     * @param pageable pagination details
     * @return a pair containing the list of items for the period and the total count
     * @throws EntityNotFoundException if the item does not exist
     */
    public Pair<List<ItemsForPeriod>, Long> getItemsForPeriod(LocalDate start, LocalDate end, long id, Pageable pageable) {
        if (!isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        var total = dbQueryDuration.record(() ->
                itemsForPeriodRepository.countItemsForPeriod(start, end, id));
        var itemsList = dbQueryDuration.record(() ->
                itemsForPeriodRepository.getItemsForPeriod(start, end, id, pageable.getPageSize(), pageable.getPageNumber()));
        log.info("Get items from {} to {}", start, end);

        return Pair.of(itemsList, total);
    }

    /**
     * Retrieves a list of all item categories.
     *
     * @return the list of item categories
     */
    public List<String> getCategories() {
        dbQueriesTotal.increment();
        log.info("Get item categories");

        return dbQueryDuration.record(itemRepository::getCategories);
    }

    /**
     * Retrieves a list of categories of the user's favourite items.
     *
     * @param username the user's email address
     * @return the list of categories of the user's favourite items
     */
    public List<String> getFavouritesCategories(String username) {
        dbQueriesTotal.increment();
        log.info("Get favourite item categories to user {}", username);

        return dbQueryDuration.record(() -> itemRepository.getFavouritesCategories(username));
    }

    /**
     * Retrieves a list of active lots for a specific item.
     *
     * @param id       the ID of the item
     * @param pageable pagination details
     * @return a pair containing the list of active lots and the total count
     * @throws EntityNotFoundException if the item does not exist
     */
    public Pair<List<Lot>, Long> getActiveLots(long id, Pageable pageable) {
        if (!isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        var total = dbQueryDuration.record(() ->
                lotRepository.countActiveLots(id));
        var lots = dbQueryDuration.record(() ->
                lotRepository.findActiveLots(id, pageable.getPageSize(), pageable.getPageNumber()));
        log.info("Get active lots from user {}", id);

        return Pair.of(lots, total);
    }

    /**
     * Retrieves a list of dependencies for a specific item.
     *
     * @param id       the ID of the item
     * @param pageable pagination details
     * @return a pair containing the list of dependencies and the total count
     * @throws EntityNotFoundException if the item does not exist
     */
    public Pair<List<Dependency>, Long> getDependencies(long id, Pageable pageable) {
        if (!isItemExists(id)) {
            throw new EntityNotFoundException("There is no item with such an identifier");
        }

        dbQueriesTotal.increment();

        var total = dbQueryDuration.record(() ->
                dependencyRepository.getDependenciesCount(id));
        var dependencyList = dbQueryDuration.record(() ->
                dependencyRepository.getDependencies(id, pageable.getPageSize(), pageable.getPageNumber()));
        log.info("Get dependencies to item {}", id);

        return Pair.of(dependencyList, total);
    }

}
