package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.metrics.DatabaseQueriesTotal;
import com.example.metrics.DatabaseQueryDuration;
import com.example.model.entity.Item;
import com.example.repository.ItemRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private DatabaseQueriesTotal dbQueriesTotal;

    @Mock
    private DatabaseQueryDuration dbQueryDuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemService(
                itemRepository,
                null,
                null,
                null,
                null,
                dbQueriesTotal,
                dbQueryDuration
        );
    }

    @Test
    void getItem_shouldReturnItem_whenExists() {
        int id = 1;
        Item item = new Item(id, "test", "");

        when(dbQueryDuration.record(any(Supplier.class))).thenAnswer(invocation -> item);

        Item result = itemService.getItem(id);

        assertNotNull(result);
        assertEquals(item, result);
        verify(dbQueriesTotal).increment();
    }

    @Test
    void isItemExists_shouldReturnTrue_whenExists() {
        long id = 1L;

        when(dbQueryDuration.record(any(Supplier.class))).thenReturn(true);

        boolean result = itemService.isItemExists(id);

        assertTrue(result);
        verify(dbQueriesTotal).increment();
    }

    @Test
    void isItemExists_shouldReturnFalse_whenNotExists() {
        long id = 1L;

        when(dbQueryDuration.record(any(Supplier.class))).thenReturn(false);

        boolean result = itemService.isItemExists(id);

        assertFalse(result);
        verify(dbQueriesTotal).increment();
    }

    @Test
    void getCategories_shouldReturnCategories() {
        List<String> categories = List.of("Category1", "Category2");

        when(dbQueryDuration.record(any(Supplier.class))).thenReturn(categories);

        List<String> result = itemService.getCategories();

        assertEquals(categories, result);
        verify(dbQueriesTotal).increment();
    }
}
