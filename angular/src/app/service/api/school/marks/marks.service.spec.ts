/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { MarksService } from './marks.service';

describe('Service: Marks', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MarksService]
    });
  });

  it('should ...', inject([MarksService], (service: MarksService) => {
    expect(service).toBeTruthy();
  }));
});
