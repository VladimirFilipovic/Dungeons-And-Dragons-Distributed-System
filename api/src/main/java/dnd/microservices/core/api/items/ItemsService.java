package dnd.microservices.core.api.items;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemsService {

        @GetMapping(value = "/items", produces = "application/json")
        Flux<Item> getItems();

        @GetMapping(value = "/items/{itemName}", produces = "application/json")
        Mono<Item> getItem(@PathVariable String itemName);

        @PostMapping(value = "/items", consumes = "application/json", produces = "application/json")
        Item createItem(@RequestBody ItemCreateDto body);

        @DeleteMapping(value = "/items/{itemName}")
        void deleteItem(@PathVariable String itemName);
}