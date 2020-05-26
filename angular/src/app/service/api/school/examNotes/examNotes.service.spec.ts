/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ExamNotesService } from './examNotes.service';

describe('Service: ExamNotes', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExamNotesService]
    });
  });

  it('should ...', inject([ExamNotesService], (service: ExamNotesService) => {
    expect(service).toBeTruthy();
  }));
});
