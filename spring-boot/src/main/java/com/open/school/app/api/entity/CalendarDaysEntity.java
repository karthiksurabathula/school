package com.open.school.app.api.entity;

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
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "calendarDays")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CalendarDaysEntity {

	@Id
	private String day;

	@Transient
	@OneToMany(targetEntity = TimeTableEntity.class, mappedBy = "calendarDay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(nullable = false)
	@JsonManagedReference(value = "timeTable")
	public List<TimeTableEntity> timeTable;

}
