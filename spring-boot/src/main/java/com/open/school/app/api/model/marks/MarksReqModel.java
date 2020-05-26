package com.open.school.app.api.model.marks;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.open.school.app.api.entity.MarksEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.SubjectEntity;

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
public class MarksReqModel {

	private StudentEntity student;
	private MarksEntity marks;
	private SubjectEntity subject;

}
