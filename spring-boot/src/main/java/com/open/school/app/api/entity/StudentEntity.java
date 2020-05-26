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
@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "student", indexes = { @Index(name = "SECONDARY", columnList = "studentId,schoolId") })
@DynamicUpdate
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class StudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = true)
	private String studentId;
	@Column(nullable = false)
	private String name;
	@Column(nullable = true)
	private String gender;
	@Column(nullable = false)
	private String dob;
	@Column(nullable = true)
	private String address;
	@Column(nullable = true)
	private String bloodGroup;
	@Column(nullable = false)
	private String fatherName;
	@Column(nullable = true)
	private String fatherPhoneNo;
	@Column(nullable = true)
	private String fatherEmail;
	@Column(nullable = false)
	private String motherName;
	@Column(nullable = true)
	private String motherPhoneNo;
	@Column(nullable = true)
	private String motherEmail;
	
	@Column(nullable = true)
	private String username;

	@Column(nullable = false)
	private boolean pending;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false, foreignKey = @ForeignKey(name = "fk_school_id_student"))
	@JsonBackReference(value = "schoolid")
	public SchoolEntity school;

	@Transient
	@OneToMany(targetEntity = StudentMapEntity.class, mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference(value = "studentid")
	public List<StudentMapEntity> studentsMap;
}
