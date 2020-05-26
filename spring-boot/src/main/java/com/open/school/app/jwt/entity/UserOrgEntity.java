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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.open.school.app.api.entity.SchoolEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "userOrgMap", indexes = { @Index(name = "SECONDARY", columnList = "loginUserId,schoolId") })
@DynamicUpdate
public class UserOrgEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loginUserId", nullable = false, foreignKey = @ForeignKey(name = "fk_login_id_userOrg"))
	@JsonBackReference(value = "loginUser")
	public LoginUser loginUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false, foreignKey = @ForeignKey(name = "fk_school_id_userOrg"))
	@JsonBackReference(value = "schoolid")
	public SchoolEntity school;

}
