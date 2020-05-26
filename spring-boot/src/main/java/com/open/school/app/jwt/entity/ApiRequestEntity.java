package com.open.school.app.jwt.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ApiRequest", indexes = { @Index(name = "SECONDARY", columnList = "request,type") })
public class ApiRequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private String request;
	@Column(nullable = false)
	private String type;
	@Column(nullable = false)
	private String javaClass;
	@Column(nullable = false)
	private String description;

	@Transient
	@OneToMany(targetEntity = EntitlementByRoleEntity.class, mappedBy = "api", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "entitlement")
	public List<EntitlementByRoleEntity> entitlement;

}
