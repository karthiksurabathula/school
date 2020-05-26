package com.open.school.app.api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "schoolCity")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SchoolCityEntity implements Serializable {

	private static final long serialVersionUID = -8861627069293453905L;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	private String state;
	private boolean status;
	@Column(nullable = false)
	private String modifiedBy;
	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy' 'HH:mm:ss")
	private Date lastModified;
	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy' 'HH:mm:ss")
	private Date createdDate;

	@Transient
	@OneToMany(targetEntity = SchoolEntity.class, mappedBy = "schoolCity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "schoolCity")
	public List<SchoolEntity> schools;

}
