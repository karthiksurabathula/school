package com.open.school.app.api.model.attendance;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.open.school.app.api.entity.AttendanceEntity;
import com.open.school.app.api.entity.AttendenceTrackerEntity;
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.StudentEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AttendanceReqModel {

	private StudentEntity student;
	private WorkingDayModel day;
	private AttendanceEntity attendance;
	@JsonProperty("class")
	private ClassEntity classes;
	private SectionEntity section;
	private AttendenceTrackerEntity attendanceTracker;
}
