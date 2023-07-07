package dnd.microservices.inventoryservice.services;

import java.util.List;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.InventoryService;
import dnd.microservices.core.api.items.inventory.ModificationType;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.ServiceUtil;
import dnd.microservices.inventoryservice.persistance.characterInventory.CharacterInventoryItemEntity;
import dnd.microservices.inventoryservice.persistance.characterInventory.CharacterInventoryItemEntityRepository;
import dnd.microservices.inventoryservice.persistance.characterInventory.CharacterInventoryItemKey;
import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryEntity;
import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryRepository;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@RestController
public class BasicInventoryService implements InventoryService {

    private final CharacterInventoryRepository characterInventoryRepository;
    private final CharacterInventoryItemEntityRepository characterInventoryItemEntityRepository;
    private final ItemRepository itemRepository;
    private final InventoryMapper inventoryMapper;
    private final ServiceUtil serviceUtil;
    private final Scheduler scheduler;

    @Autowired
    public BasicInventoryService(
            CharacterInventoryRepository characterInventoryRepository,
            CharacterInventoryItemEntityRepository characterInventoryItemEntityRepository,
            ItemRepository itemRepository,
            InventoryMapper inventoryMapper, 
            ServiceUtil serviceUtil,
            Scheduler scheduler
        ) {
        this.characterInventoryRepository = characterInventoryRepository;
        this.characterInventoryItemEntityRepository = characterInventoryItemEntityRepository;
        this.itemRepository = itemRepository;
        this.inventoryMapper = inventoryMapper;
        this.serviceUtil = serviceUtil;
        this.scheduler = scheduler;
    }

    @Override
    public Flux<InventoryItem> getCharacterInventory(String characterId) {
        return asyncFlux(() -> Flux.fromIterable(this.getInventoryByCharacterId(characterId)));
    }

    protected List<InventoryItem> getInventoryByCharacterId(String characterId) {
        return this.characterInventoryRepository
            .findByCharacterId(characterId)
            .map(inventoryEntity -> inventoryMapper.entityToApi(inventoryEntity.items))
            .orElseThrow(() -> new NotFoundException("No inventory found for characterId: " + characterId));
    }

    @Override
    
    public void modifyCharacterInventory(String characterId, InventoryItemModificationDto body) {
        this.characterInventoryItemEntityRepository
            .findById(new CharacterInventoryItemKey(characterId, body.itemId))
            .ifPresentOrElse(
                characterInventoryItemEntity -> {
                    if (body.modificationType == ModificationType.ADD) {
                        characterInventoryItemEntity.quantity += body.amount;
                    } else {
                        characterInventoryItemEntity.quantity -= body.amount;
                    }
                    this.characterInventoryItemEntityRepository.save(characterInventoryItemEntity);
                },
                () -> {
                    if (body.modificationType == ModificationType.ADD) {
                        CharacterInventoryEntity inventoryEntity = characterInventoryRepository
                            .findByCharacterId(characterId)
                            .orElseThrow(() -> new NotFoundException("No inventory found for characterId: " + characterId));

                        ItemEntity itemEntity = itemRepository.findById(body.itemId)
                            .orElseThrow(() -> new NotFoundException("No item found with itemId: " + body.itemId));

                        CharacterInventoryItemEntity characterInventoryItemEntity = new CharacterInventoryItemEntity(
                            inventoryEntity,
                            itemEntity,
                            body.amount
                        );
                         this.characterInventoryItemEntityRepository.save(characterInventoryItemEntity);
                    }
                    else {
                        throw new NotFoundException("No item found with itemId: " + body.itemId + " for characterId: " + characterId);
                    }
                   
                }
            );
    }

    @Override
    public void deleteCharacterInventory(String characterId) {
        CharacterInventoryEntity inventoryEntity = characterInventoryRepository
                .findByCharacterId(characterId)
                .orElseThrow(() -> new NotFoundException("No inventory found for characterId: " + characterId));
        
        characterInventoryRepository.delete(inventoryEntity);
    }

    @Override
    public void createCharacterInventory(String characterId) {
        CharacterInventoryEntity inventoryEntity = new CharacterInventoryEntity(characterId);
        characterInventoryRepository.save(inventoryEntity);
    }

    private <T> Flux<T> asyncFlux(Supplier<Publisher<T>> publisherSupplier) {
        return Flux.defer(publisherSupplier).subscribeOn(scheduler);
    }

}
