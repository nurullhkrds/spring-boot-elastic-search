package com.elasticsearch.elasticsearchsample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;


@Document(indexName = "items_index")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Setting(settingPath = "static/es-settings.json")
public class Item {

    @Id
    private String id;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "price", type = FieldType.Double)
    private Double price;

    @Field(name = "brand", type = FieldType.Text)
    private String brand;

    @Field(name = "category", type = FieldType.Keyword)
    private String category;
    




}
