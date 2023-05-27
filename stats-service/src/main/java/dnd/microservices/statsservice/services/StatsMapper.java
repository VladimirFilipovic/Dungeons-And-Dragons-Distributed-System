package dnd.microservices.statsservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.statsservice.persistance.Stats.StatsEntity;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    
    Statistic entityToApi(StatsEntity entity);
    
    @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)
    })        StatsEntity apiToEntity(Statistic api);
}


