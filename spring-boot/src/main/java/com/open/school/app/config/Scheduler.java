package com.open.school.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.open.school.app.api.service.ExamServiceImpl;

@Component
public class Scheduler {

	@Autowired
	private ExamServiceImpl examService;
	
	//every 1 minute
//	@Scheduled(cron = "0 0/1 * * * ?")
	@Scheduled(cron = "0 0 1 * * ?")
	public void scheduler() {
		examService.checkExamCompletionStatus();
	}
}
