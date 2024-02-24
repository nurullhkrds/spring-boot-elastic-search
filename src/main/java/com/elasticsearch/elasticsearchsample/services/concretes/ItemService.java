package com.elasticsearch.elasticsearchsample.services.concretes;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearch.elasticsearchsample.DTOs.SearchRequestDto;
import com.elasticsearch.elasticsearchsample.model.Item;
import com.elasticsearch.elasticsearchsample.repository.ItemRepository;
import com.elasticsearch.elasticsearchsample.services.interfaces.IItemService;
import com.elasticsearch.elasticsearchsample.utilities.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Override
    public Item createIndex(Item item) {

        return itemRepository.save(item);
    }

    @Override
    public Item getIndex(String id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Item> getAllIndex() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getAllDataFromIndex(String indexName)  {
        var query= ElasticSearchUtil.createMatchAllQuery(indexName);
        log.info("ElasticSearch Query: {}", query.toString());
        SearchResponse<Item> searchResponse= null;
        try {
            searchResponse = elasticsearchClient.search(
                    q->q.index(indexName).query(query), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("ElasticSearch Response: {}", searchResponse.toString());
        return extractItemsFromResponse(searchResponse);
    }

    @Override
    public List<Item> searchItemsByFieldAndValue(SearchRequestDto searchRequestDto) {
        Supplier<Query> query=ElasticSearchUtil.buildQueryForFieldAndValue
                (searchRequestDto.getFieldName().get(0),
                        searchRequestDto.getSearchValue().get(0));
        log.info("ElasticSearch Query: {}", query.get().toString());
        SearchResponse<Item> searchResponse= null;
        try {
            searchResponse = elasticsearchClient.search(
                    q->q.index("items_index").query(query.get()), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("ElasticSearch Response: {}", searchResponse.toString());
        return extractItemsFromResponse(searchResponse);

    }

    @Override
    public Set<Item> findSuggestedItemNames(String itemName) {
        Query autoSuggestQuery = ElasticSearchUtil.buildAutoSuggestQuery(itemName);
        log.info("Elasticsearch query: {}", autoSuggestQuery.toString());

        try {
            return elasticsearchClient.search(q -> q.index("items_index").query(autoSuggestQuery), Item.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> autoSuggestItemsByNameWithQuery(String name) {
        List<Item> items = itemRepository.customAutoSuggestQuery(name);
        log.info("ElastichSearch Response: {}", items.toString());
        return items.stream()
                .map(Item::getName)
                .collect(Collectors.toList());
    }


    public List<Item> extractItemsFromResponse(SearchResponse<Item> searchResponse) {
        return searchResponse
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }


}
