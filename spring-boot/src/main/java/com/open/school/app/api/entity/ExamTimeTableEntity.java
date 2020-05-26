package com.open.school.app.api.entity;

import java.util.Date;

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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@Table(name = "examTimetable", indexes = { @Index(name = "SECONDARY", columnList = "subjectId,schoolId,examId") })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ExamTimeTableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private int marks;
	@Column(nullable = true)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date createdDate;
	@Column(nullable = false)
	private String modifiedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false, foreignKey = @ForeignKey(name = "fk_schoolid_examTimetable"))
	@JsonBackReference(value = "schoolid")
	public SchoolEntity school;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classId", nullable = true, foreignKey = @ForeignKey(name = "fk_class_id_examTimetable"))
	@JsonBackReference(value = "classes")
	public ClassEntity classes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectId", nullable = false, foreignKey = @ForeignKey(name = "fk_subject_id_examTimetable"))
	@JsonBackReference(value = "subject")
	public SubjectEntity subject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examId", nullable = false, foreignKey = @ForeignKey(name = "fk_exam_examTimetable"))
	@JsonBackReference(value = "examTimetable")
	public ExamEntity exam;

}
