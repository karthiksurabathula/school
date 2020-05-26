/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ExamTimetableService } from './exam-timetable.service';

describe('Service: ExamTimetable', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExamTimetableService]
    });
  });

  it('should ...', inject([ExamTimetableService], (service: ExamTimetableService) => {
    expect(service).toBeTruthy();
  }));
});
