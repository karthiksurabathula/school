/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { InitTrackerService } from './initTracker.service';

describe('Service: InitTracker', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InitTrackerService]
    });
  });

  it('should ...', inject([InitTrackerService], (service: InitTrackerService) => {
    expect(service).toBeTruthy();
  }));
});
