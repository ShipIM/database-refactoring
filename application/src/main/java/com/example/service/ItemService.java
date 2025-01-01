package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.entity.Dependency;
import com.example.model.entity.Item;
import com.example.model.entity.ItemsForPeriod;
import com.example.model.entity.Lot;
import com.example.repository.DependencyRepository;
import com.example.repository.ItemRepository;
import com.example.repository.ItemsForPeriodRepository;
import com.example.repository.LotRepository;
import lombok.RequiredArgsConstructor;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final LotRepository lotRepository;
    private final ItemsForPeriodRepository itemsForPeriodRepository;
    private final DependencyRepository dependencyRepository;
    private final DetailsService detailsService;

    /**
     * Retrieves a filtered list of items based on the provided name and category.
     *
     * @param name     the name filter for items
     * @param category the category filter for items
     * @param pageable pagination details
     * @return a pair containing the list of items and the total count
     */
    public Pair<List<Item>, Long> getItems(String name, String category, Pageable pageable) {
        var total = itemRepository.countFilteredItems(name, category);
        var items = itemRepository
                .findFilteredItems(name, category, pageable.getPageSize(), pageable.getPageNumber());

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
        return itemRepository.findItem(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no item with such an identifier"));
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

        var total = itemRepository.countFavouriteItems(email, name, category);
        var items = itemRepository
                .findFavouriteItems(email, name, category, pageable.getPageSize(), pageable.getPageNumber());

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

        return itemRepository.isFavourite(email, id);
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

        itemRepository.addFavouriteItem(username, id);
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

        itemRepository.deleteFavouriteItem(username, id);
    }

    /**
     * Checks if an item exists by its ID.
     *
     * @param id the ID of the item to check
     * @return true if the item exists, false otherwise
     */
    public boolean isItemExists(long id) {
        return itemRepository.isItemExists(id);
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

        return itemRepository.getSelfprice(id).orElseThrow(() ->
                new EntityNotFoundException("It is impossible to calculate the self price"));
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

        var total = itemsForPeriodRepository.countItemsForPeriod(start, end, id);
        var itemsList = itemsForPeriodRepository
                .getItemsForPeriod(start, end, id, pageable.getPageSize(), pageable.getPageNumber());

        return Pair.of(itemsList, total);
    }

    /**
     * Retrieves a list of all item categories.
     *
     * @return the list of item categories
     */
    public List<String> getCategories() {
        return itemRepository.getCategories();
    }

    /**
     * Retrieves a list of categories of the user's favourite items.
     *
     * @param username the user's email address
     * @return the list of categories of the user's favourite items
     */
    public List<String> getFavouritesCategories(String username) {
        return itemRepository.getFavouritesCategories(username);
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

        var total = lotRepository.countActiveLots(id);
        var lots = lotRepository
                .findActiveLots(id, pageable.getPageSize(), pageable.getPageNumber());

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

        var total = dependencyRepository.getDependenciesCount(id);
        var dependencyList = dependencyRepository
                .getDependencies(id, pageable.getPageSize(), pageable.getPageNumber());

        return Pair.of(dependencyList, total);
    }

}
