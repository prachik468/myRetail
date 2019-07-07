package com.myretail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "price")
public class Price {

	@JsonIgnore
	@Id
	private long priceId;

	@JsonIgnore
	private long id;

	private Double value;

	@JsonProperty("currency_code")
	@Field("currency_code")
	private String currCode;

	public long getPriceId() {
		return priceId;
	}

	public void setPriceId(long priceId) {
		this.priceId = priceId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public Price() {
	}
	
	public Price(long id, Double value, String currCode) {
		super();
		this.id = id;
		this.value = value;
		this.currCode = currCode;
	}
}
