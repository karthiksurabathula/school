/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { PeriodService } from './period.service';

describe('Service: Period', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PeriodService]
    });
  });

  it('should ...', inject([PeriodService], (service: PeriodService) => {
    expect(service).toBeTruthy();
  }));
});
