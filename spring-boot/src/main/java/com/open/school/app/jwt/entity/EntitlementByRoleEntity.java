package com.open.school.app.jwt.entity;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "entitlement", indexes = { @Index(name = "SECONDARY", columnList = "role,apiId") })
public class EntitlementByRoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private boolean access;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role", nullable = false, foreignKey = @ForeignKey(name = "fk_role_entitlement"))
	@JsonBackReference(value = "role")
	public UserRoleEntity role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apiId", nullable = false, foreignKey = @ForeignKey(name = "fk_api_id_entitlement"))
	@JsonBackReference(value = "entitlement")
	public ApiRequestEntity api;

}
