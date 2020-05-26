package com.open.school.app.jwt.entity;

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
@Table(name = "customEntitlement", indexes = { @Index(name = "SECONDARY", columnList = "loginUserId,apiId") })
public class CustomEntitlementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apiId", nullable = false, foreignKey = @ForeignKey(name = "fk_api_id_customEntitlement"))
	@JsonBackReference(value = "entitlement")
	public ApiRequestEntity api;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loginUserId", nullable = false, foreignKey = @ForeignKey(name = "fk_login_id_customEntitlement"))
	@JsonBackReference(value = "loginUser")
	public LoginUser loginUser;

}
