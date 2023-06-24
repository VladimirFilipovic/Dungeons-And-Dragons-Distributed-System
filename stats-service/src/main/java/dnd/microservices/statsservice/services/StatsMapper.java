package dnd.microservices.statsservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsName;
import dnd.microservices.statsservice.persistance.Stats.StatsEntity;
import dnd.microservices.statsservice.persistance.Stats.StatsKey;

@Mapper(componentModel = "spring")
public interface StatsMapper {
     @Mappings({
        @Mapping(target = "name", expression = "java(idToName(entity))")
     })
    Statistic entityToApi(StatsEntity entity);
    
    @Mappings({
        @Mapping(target = "id", expression = "java(createId(characterId, api))"),
        @Mapping(target = "serviceAddress", ignore = true)
    })        StatsEntity apiToEntity(String characterId, Statistic api);

    
    default StatsKey createId(String characterId, Statistic api) {
        // Customize the ID creation logic based on the name and value of the entity
        return new StatsKey(characterId, api.name.name());
    }

    default StatsName idToName(StatsEntity entity) {
        // Customize the ID creation logic based on the name and value of the entity
        return StatsName.valueOf(entity.id.statisticName);
    }
}


