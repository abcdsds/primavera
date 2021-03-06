package com.genius.primavera.domain.model.hierarchy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@ToString(callSuper = true)
@Table(name = "ADDRESS")
@DiscriminatorValue("A")
public class Address extends Contact {
	@Column(name = "COUNTRY")
	private String country;
	@Column(name = "CITY")
	private String city;
	@Column(name = "STREET")
	private String street;
	@Column(name = "ZIPCODE")
	private String zipCode;
}
