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
@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "period", indexes = { @Index(name = "SECONDARY", columnList = "schoolId") })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PeriodEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private long startTime;
	@Column(nullable = false)
	private long endTime;

	@Column(nullable = false)
	private String modifiedBy;
	@Column(nullable = false)
	@JsonFormat(pattern = "dd-MM-yyyy' 'HH:mm:ss")
	private Date lastModified;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false, foreignKey = @ForeignKey(name = "fk_school_id_period"))
	@JsonBackReference(value = "schoolid")
	public SchoolEntity school;

	@Transient
	@OneToMany(targetEntity = TimeTableEntity.class, mappedBy = "period", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "period")
	public List<TimeTableEntity> timeTable;

	public Date getStartTime() {
		return new Date(startTime);
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime.getTime();
	}

	public Date getEndTime() {
		return new Date(endTime);
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime.getTime();
	}

}
