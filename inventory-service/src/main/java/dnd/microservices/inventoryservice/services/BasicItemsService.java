package dnd.microservices.inventoryservice.services;

import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.ServiceUtil;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import org.reactivestreams.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class BasicItemsService implements ItemsService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ServiceUtil serviceUtil;
    private final Scheduler scheduler;


    @Autowired
    public BasicItemsService(ItemRepository itemRepository, ItemMapper itemMapper, ServiceUtil serviceUtil, Scheduler scheduler) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.serviceUtil = serviceUtil;
        this.scheduler = scheduler;
    }
 
    /**
     * @param characterName
     * @return
     */
    @Override
    public Flux<Item> getItems() {
        return asyncFlux(() -> Flux.fromIterable(this.getItemsFromDb()));
    }

    protected List<Item> getItemsFromDb() {
        Stream<ItemEntity> itemEntitiesStream = StreamSupport.stream(itemRepository.findAll().spliterator(), false);

        return itemEntitiesStream
                .map(itemMapper::entityToApi)
                .peek(item -> item.setServiceAddress(serviceUtil.getServiceAddress()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Mono<Item> getItem(String itemName) {
        return asyncMono(() -> Mono.justOrEmpty(this.getItemByName(itemName)));
    }

    protected Item getItemByName(String itemName) {
        ItemEntity itemEntity = itemRepository.findByName(itemName)
          .orElseThrow(() -> new NotFoundException("No item found for name: " + itemName));

        Item item = itemMapper.entityToApi(itemEntity);
        item.setServiceAddress(serviceUtil.getServiceAddress());
        
        return item;
    }

    @Override
    public Item createItem(ItemCreateDto body) {
        System.out.println("BasicItemsService.createItem: " + body);
        ItemEntity itemEntity = itemMapper.itemCreateDtoToItemEntity(body);
        try {
            ItemEntity newItemEntity = itemRepository.save(itemEntity);
            return itemMapper.entityToApi(newItemEntity);
        } catch (DuplicateKeyException dke) {
            throw new IllegalArgumentException("Duplicate key, Item name: " + body.name);
        }
    }

    @Override
    public void deleteItem(String itemName) {
        ItemEntity item = itemRepository.findByName(itemName)
          .orElseThrow(() -> new NotFoundException("No item found for name: " + itemName));
        itemRepository.delete(item);
    }


     private <T> Flux<T> asyncFlux(Supplier<Publisher<T>> publisherSupplier) {
        return Flux.defer(publisherSupplier).subscribeOn(scheduler);
    }

     private <T> Mono<T> asyncMono(Supplier<? extends Mono<? extends T>> publisherSupplier) {
        return Mono.defer(publisherSupplier).subscribeOn(scheduler);
    }
}
