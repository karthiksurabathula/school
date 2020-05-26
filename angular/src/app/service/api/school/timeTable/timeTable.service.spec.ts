/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { TimeTableService } from './timeTable.service';

describe('Service: TimeTable', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TimeTableService]
    });
  });

  it('should ...', inject([TimeTableService], (service: TimeTableService) => {
    expect(service).toBeTruthy();
  }));
});
