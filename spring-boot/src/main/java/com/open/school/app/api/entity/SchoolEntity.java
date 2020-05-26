package com.open.school.app.api.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "school", indexes = { @Index(name = "SECONDARY", columnList = "schoolCode,schoolCity") })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SchoolEntity {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = true)
	private String schoolCode;
	@Column(nullable = false)
	private String schoolName;
	@Column(nullable = false)
	private String schoolPhone;
	@Column(nullable = false)
	private String schoolEmail;
	@Column(nullable = false)
	private String location;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
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
	@OneToMany(targetEntity = StaffEntity.class, mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "schoolid")
	public List<StaffEntity> staff;

	@Transient
	@OneToMany(targetEntity = ClassEntity.class, mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "schoolid")
	public List<ClassEntity> classes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolCity", nullable = false, foreignKey = @ForeignKey(name = "fk_city_id_school"))
	@JsonBackReference(value = "schoolCity")
	public SchoolCityEntity schoolCity;

}
