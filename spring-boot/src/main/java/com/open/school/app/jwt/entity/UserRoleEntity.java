package com.open.school.app.jwt.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.open.school.app.api.entity.StaffEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "userRole")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserRoleEntity {

	@Id
	@Column(nullable = false)
	private String role;

	@Transient
	@OneToMany(targetEntity = StaffEntity.class, mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "role")
	public List<StaffEntity> staff;
}
