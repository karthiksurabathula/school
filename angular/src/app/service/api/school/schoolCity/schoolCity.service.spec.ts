/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SchoolCityService } from './schoolCity.service';

describe('Service: SchoolCity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SchoolCityService]
    });
  });

  it('should ...', inject([SchoolCityService], (service: SchoolCityService) => {
    expect(service).toBeTruthy();
  }));
});
