package com.example.mapper;

import com.example.dto.item.*;
import com.example.model.entity.Dependency;
import com.example.model.entity.Item;
import com.example.model.entity.ItemsForPeriod;
import com.example.model.entity.Lot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemViewResponse mapToViewResponse(Item item);

    ItemResponse mapToResponse(Item item);

    LotResponse mapToResponse(Lot lot);

    DependencyResponse mapToResponse(Dependency dependency);

    ItemsForPeriodResponse mapToResponse(ItemsForPeriod items);

}
