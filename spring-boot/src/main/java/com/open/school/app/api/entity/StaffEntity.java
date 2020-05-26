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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.open.school.app.jwt.entity.UserRoleEntity;

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
@Table(name = "staff", indexes = { @Index(name = "SECONDARY", columnList = "schoolId,role") })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@DynamicUpdate
public class StaffEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = true)
	private String gender;
	@Column(nullable = true)
	private String bloodGroup;
	@Column(nullable = true)
	private String dob;
	@Column(nullable = true)
	private String address;
	@Column(nullable = true)
	private String qualification;
	@Column(nullable = true)
	private String phoneNo;
	@Column(nullable = true)
	private String email;
	@Column(nullable = false)
	private String modifiedBy;
	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy' 'HH:mm:ss")
	private Date lastModified;
	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy' 'HH:mm:ss")
	private Date createdDate;
	@Column(nullable = false)
	private boolean status;
	@Column(nullable = false)
	private String staffId;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "schoolId", nullable = false, foreignKey = @ForeignKey(name = "fk_schoolid_staff"))
	@JsonBackReference(value = "schoolid")
	public SchoolEntity school;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role", nullable = false, foreignKey = @ForeignKey(name = "fk_role_staff"))
	@JsonBackReference(value = "role")
	public UserRoleEntity role;

	@Transient
	@OneToMany(targetEntity = SubjectStaffMapEntity.class, mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "staff")
	public List<SubjectStaffMapEntity> staffMap;

}
