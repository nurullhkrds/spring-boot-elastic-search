package com.elasticsearch.elasticsearchsample.services.interfaces;

import com.elasticsearch.elasticsearchsample.DTOs.SearchRequestDto;
import com.elasticsearch.elasticsearchsample.model.Item;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IItemService {
    Item createIndex(Item item);

    Item getIndex(String id);

    Iterable<Item> getAllIndex();

    List<Item> getAllDataFromIndex(String indexNane) throws IOException;

    List<Item> searchItemsByFieldAndValue(SearchRequestDto searchRequestDto);

    Set<Item> findSuggestedItemNames(String name);

    List<String> autoSuggestItemsByNameWithQuery(String name);
}
