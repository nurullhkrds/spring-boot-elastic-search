package com.elasticsearch.elasticsearchsample.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequestDto {

    private List<String> fieldName;

    private List<String> searchValue;

}
