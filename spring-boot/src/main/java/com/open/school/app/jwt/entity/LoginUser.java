package com.open.school.app.jwt.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.open.school.app.api.entity.SchoolEntity;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users", indexes = { @Index(name = "SECONDARY", columnList = "schoolId") })
@DynamicUpdate
@JsonInclude(Include.NON_NULL)
public class LoginUser {

	@JsonIgnore
	@Id
	@NotNull
	private String username;
	@NotNull
	@JsonIgnore
	private String password;
	@NotNull
	@JsonIgnore
	private boolean accountNonExpired;
	@NotNull
	@JsonIgnore
	private boolean accountNonLocked;
	@NotNull
	@JsonIgnore
	private boolean credentialsNonExpired;
	@JsonIgnore
	@NotNull
	private boolean enabled;
	@JsonIgnore
	@NotNull
	private Date createdDate;
	@JsonIgnore
	@NotNull
	private Date modifiedDate;
	@JsonIgnore
	@Lob
	private String token;
	@JsonIgnore
	private Date tokenCreatedDate;
	
	@NotNull
	private boolean customPrevilages;

	@NotNull
	private boolean resetPassword;
	private String restetToken;
	private Date restetTokenCreatedDate;
	
	
	@Column(nullable=true)
	private int failurecount;
	@Column(nullable=true)
	private Date lastLoginFailureTime;
	@Column(nullable=true)
	private String lastLoginFailureIpAddress;

	@Transient
	@OneToMany(targetEntity = CustomEntitlementEntity.class, mappedBy = "loginUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "loginUser")
	public List<CustomEntitlementEntity> customEntitlement;

	@ManyToOne
	@JoinColumn(name = "role", nullable = false, foreignKey = @ForeignKey(name = "fk_role_user"))
	@JsonBackReference(value = "userRole")
	public UserRoleEntity role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = true, foreignKey = @ForeignKey(name = "fk_schoolid_LoginUser"))
	@JsonBackReference(value = "schoolid")
	public SchoolEntity school;

}
