/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { UserRolesService } from './userRoles.service';

describe('Service: UserRoles', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserRolesService]
    });
  });

  it('should ...', inject([UserRolesService], (service: UserRolesService) => {
    expect(service).toBeTruthy();
  }));
});
