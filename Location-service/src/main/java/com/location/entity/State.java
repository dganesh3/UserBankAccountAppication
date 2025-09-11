package com.location.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;
@JsonIgnoreProperties({"districts"})
@Entity
@Data
public class State {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String stateName;
	
	@ManyToOne
	@JoinColumn
	@JsonBackReference
	private Country country;
	@OneToMany(mappedBy = "state",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<District> districts;
	

}
