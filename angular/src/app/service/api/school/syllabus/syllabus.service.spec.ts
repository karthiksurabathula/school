/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SyllabusService } from './syllabus.service';

describe('Service: Syllabus', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SyllabusService]
    });
  });

  it('should ...', inject([SyllabusService], (service: SyllabusService) => {
    expect(service).toBeTruthy();
  }));
});
