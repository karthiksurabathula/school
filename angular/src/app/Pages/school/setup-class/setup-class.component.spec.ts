/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SetupClassComponent } from './setup-class.component';

describe('SetupClassComponent', () => {
  let component: SetupClassComponent;
  let fixture: ComponentFixture<SetupClassComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SetupClassComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetupClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
