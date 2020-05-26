/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AppStatusService } from './appStatus.service';

describe('Service: AppStatus', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppStatusService]
    });
  });

  it('should ...', inject([AppStatusService], (service: AppStatusService) => {
    expect(service).toBeTruthy();
  }));
});
