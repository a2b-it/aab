package com.apiweather.app.inner.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;


/**
 * @author a.bouabidi
 *
 */
@Document(collection = "database_sequences")
@Getter
@Setter
public class DataBaseSequence {
	@Id
    private String id;

    private long seq;

}
