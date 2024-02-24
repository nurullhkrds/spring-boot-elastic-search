package com.elasticsearch.elasticsearchsample.controller;

import com.elasticsearch.elasticsearchsample.DTOs.SearchRequestDto;
import com.elasticsearch.elasticsearchsample.model.Item;
import com.elasticsearch.elasticsearchsample.services.interfaces.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {


    private final IItemService itemService;



    @PostMapping("add")
    public Item createIndex(@RequestBody Item item) {
        return itemService.createIndex(item);
    }

    @GetMapping("get")
    public Item getIndex(@RequestParam String id) {
        return itemService.getIndex(id);
    }

    @GetMapping("getAllDataFromIndex/{indexName}")
    public List<Item> getAllDataFromIndex(@PathVariable String indexName) throws IOException {
        return itemService.getAllDataFromIndex(indexName);
    }

    @GetMapping("search")
    public List<Item> searchItemsByFieldAndValue(@RequestBody SearchRequestDto searchRequestDto) {
        return itemService.searchItemsByFieldAndValue(searchRequestDto);
    }


    @GetMapping("tahmin/{name}")
    public Set<Item> autoSuggestItemsByName(@PathVariable String name)  {
        return itemService.findSuggestedItemNames(name);
    }


    @GetMapping("autoSuggestQuery/{name}")
    public List<String> autoSuggestQuery(@PathVariable String name) {
        return itemService.autoSuggestItemsByNameWithQuery(name);
    }

}
