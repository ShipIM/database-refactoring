package com.example.controller;

import com.example.dto.error.ErrorResponse;
import com.example.dto.item.*;
import com.example.dto.page.PaginationRequest;
import com.example.mapper.ItemMapper;
import com.example.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "items", description = "A controller for controlling objects")
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @GetMapping
    @Operation(description = "Get all existing items with possible filtering by category and name",
            summary = "Retrieve Items", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Page<ItemViewResponse> getItems(
            @Parameter(description = "The category to filter items by")
            String category,
            @Parameter(description = "The name to filter items by")
            String name,
            @Valid PaginationRequest paginationRequest
    ) {
        var pagination = paginationRequest.formPageRequest();

        var items = itemService.getItems(name, category, pagination);

        var itemResponses = items.getKey().stream()
                .map(itemMapper::mapToViewResponse)
                .toList();

        return new PageImpl<>(itemResponses, pagination, items.getValue());
    }

    @GetMapping("/{id}")
    @Operation(description = "Get an item by ID", summary = "Get Item by ID", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ItemResponse getItem(
            @Parameter(description = "The ID of the item to retrieve", required = true, example = "123")
            @PathVariable
            @Pattern(regexp = "^(?!0+$)\\d{1,19}$", message = "The item ID must be a positive number of type long")
            String id) {
        var itemId = Long.parseLong(id);
        var item = itemService.getItem(itemId);

        var responseItem = itemMapper.mapToResponse(item);

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            responseItem.setIsFavourite(itemService.isFavourite(user.getUsername(), itemId));
        }

        return responseItem;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favourites")
    @Operation(description = "Get a list of the user's favorite items",
            summary = "Retrieve Favourite Items", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorite items retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Page<ItemViewResponse> getFavouriteItems(
            @Parameter(description = "The name to filter favourite items by")
            String name,
            @Parameter(description = "The category to filter favourite items by")
            String category,
            @Valid PaginationRequest paginationRequest) {
        var pagination = paginationRequest.formPageRequest();

        var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var items = itemService
                .getFavouriteItems(user.getUsername(), name, category, paginationRequest.formPageRequest());

        var itemResponses = items.getKey().stream()
                .map(itemMapper::mapToViewResponse)
                .toList();

        return new PageImpl<>(itemResponses, pagination, items.getValue());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/favourites")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(description = "Add an item to favorites", summary = "Add Item to Favorites", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added to favorites successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Validation errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void addFavouriteItem(@RequestBody @Valid AddFavouriteRequest dto) {
        var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        itemService.addFavouriteItem(user.getUsername(), Long.parseLong(dto.getItemId()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/favourites/{id}")
    @Operation(description = "Delete an item from favorites", summary = "Delete Item from Favorites", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item deleted from favorites successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item not found in favorites",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void deleteFavouriteItem(@PathVariable("id") String id) {
        var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        itemService.deleteFavouriteItem(user.getUsername(), Long.parseLong(id));
    }

    @GetMapping("/self-price/{id}")
    @Operation(description = "Get the cost of an item based on its components",
            summary = "Get Item Self-Price",
            tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item self-price retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public long getSelfPrice(
            @Parameter(description = "The ID of the item to retrieve the self-price for",
                    required = true, example = "123")
            @PathVariable
            @Pattern(regexp = "^(?!0+$)\\d{1,19}$", message = "The item ID must be a positive number of type long")
            String id) {
        return itemService.getSelfPrice(Long.parseLong(id));
    }

    @PostMapping("/items-for-period")
    @Operation(description = "Get the maximum price of the product per day for a given period",
            summary = "Get Items for Period",
            tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items for period retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Validation errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Page<ItemsForPeriodResponse> getItemsForPeriod(
            @RequestBody @Valid ItemsForPeriodRequest dto,
            @RequestParam @Valid PaginationRequest paginationRequest
    ) {
        var pagination = paginationRequest.formPageRequest();

        var items = itemService
                .getItemsForPeriod(dto.getStart(), dto.getEnd(), Long.parseLong(dto.getItemId()), pagination);

        var itemResponses = items.getKey().stream()
                .map(itemMapper::mapToResponse)
                .toList();

        return new PageImpl<>(itemResponses, pagination, items.getValue());
    }

    @GetMapping("/categories")
    @Operation(description = "Get the categories of items that exist in the database",
            summary = "Retrieve Item Categories", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<String> getExistingCategories() {
        return itemService.getCategories();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favourites/categories")
    @Operation(description = "Get the categories of favourite items that exist in the database",
            summary = "Retrieve Favourite Item Categories", tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorite categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<String> getExistingFavouritesCategories() {
        var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return itemService.getFavouritesCategories(user.getUsername());
    }

    @GetMapping("/{id}/lots")
    @Operation(description = "Get active item lots",
            summary = "Get Active Lots for Item",
            tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active lots retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Page<LotResponse> getActiveLots(
            @Parameter(description = "The ID of the item to retrieve active lots for",
                    required = true, example = "123")
            @PathVariable("id") String id,
            @Valid PaginationRequest paginationRequest) {
        var pagination = paginationRequest.formPageRequest();

        var lots = itemService.getActiveLots(Long.parseLong(id), pagination);

        var lotResponses = lots.getKey().stream()
                .map(itemMapper::mapToResponse)
                .toList();

        return new PageImpl<>(lotResponses, pagination, lots.getValue());
    }

    @GetMapping("/{id}/dependencies")
    @Operation(description = "Get item dependencies",
            summary = "Get Item Dependencies",
            tags = {"items"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item dependencies retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Page<DependencyResponse> getDependencies(
            @Parameter(description = "The ID of the item to retrieve dependencies for",
                    required = true, example = "123")
            @PathVariable
            @Pattern(regexp = "^(?!0+$)\\d{1,19}$", message = "The item ID must be a positive number of type long")
            String id,
            @Valid PaginationRequest paginationRequest) {
        var pagination = paginationRequest.formPageRequest();

        var dependencies = itemService
                .getDependencies(Long.parseLong(id), pagination);

        var dependencyResponses = dependencies.getKey().stream()
                .map(itemMapper::mapToResponse)
                .toList();

        return new PageImpl<>(dependencyResponses, pagination, dependencies.getValue());
    }
}
