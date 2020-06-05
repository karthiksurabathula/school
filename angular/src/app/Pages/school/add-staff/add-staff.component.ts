import { UserRolesService } from 'src/app/service/api/school/userRoles/userRoles.service';
import { StaffService } from 'src/app/service/api/school/staff/staff.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import {
  ToastService,
  ModalDirective,
  IMyOptions,
} from 'ng-uikit-pro-standard';
import { Router } from '@angular/router';
import { GlobalService } from 'src/app/service/global/global.service';
import { SchoolService } from 'src/app/service/api/school/school/school.service';
import { SchoolCityService } from 'src/app/service/api/school/schoolCity/schoolCity.service';
import { city } from 'src/app/model/school/cityResponse';
import { school } from 'src/app/model/school/schoolResponse';
import { role } from 'src/app/model/school/userRolesResponse';
import { staff } from 'src/app/model/school/staffResponse';

@Component({
  selector: 'app-add-staff',
  templateUrl: './add-staff.component.html',
  styleUrls: ['./add-staff.component.css'],
})
export class AddStaffComponent implements OnInit {
  @ViewChild('basicModal', { static: true }) basicModal: ModalDirective;
  @ViewChild('conformationModal', { static: true })
  conformationModal: ModalDirective;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private globalService: GlobalService,
    private staffService: StaffService,
    private userRolesService: UserRolesService
  ) {}

  role: string;

  ceStaffName = '';
  ceDob = '';
  cePhone = '';
  ceGender = '';
  ceBloodGroup: string;
  ceStaffAddress = '';
  ceStaffQualification: string;
  ceStaffEmail: string;
  cestatus = true;
  ceStaffRoleId = '';
  ceStaffId: number;

  updateFlag = false;
  searchText: string;

  gender: Array<any>;
  bloodGroup: Array<any>;

  cities: Array<city>;
  schools: Array<school>;
  roles: Array<role>;
  staff: Array<staff>;

  ceCityId: number;
  ceSchoolId: number;

  popupTitle: string;

  public myDatePickerOptions: IMyOptions = {
    closeAfterSelect: true,
    dateFormat: 'd-m-yyyy',
  };

  ngOnInit() {
    this.globalService.showNavBar();

    // Date
    this.gender = [
      { value: 'male', label: 'Male' },
      { value: 'female', label: 'Female' },
    ];

    this.bloodGroup = [
      { value: 'A+', label: 'A+' },
      { value: 'A-', label: 'A-' },
      { value: 'B+', label: 'B+' },
      { value: 'B-', label: 'B-' },
      { value: 'O+', label: 'O+' },
      { value: 'O-', label: 'O-' },
      { value: 'AB+', label: 'AB+' },
      { value: 'AB-', label: 'AB-' },
    ];

    this.ceSchoolId = 0;

    if (
      this.cookie.check('token') ||
      this.cookie.get('role') ||
      this.cookie.get('schoolId')
    ) {
      this.role = this.cookie.get('role');
      if (this.cookie.get('role') === 'SUPERUSER') {
        this.getcities();
      } else {
        this.ceSchoolId = Number(this.cookie.get('schoolId'));
        this.getStaffBySchool(this.ceSchoolId);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  changeStatus() {
    this.cestatus = !this.cestatus;
  }

  onGenderSelected(event) {
    this.ceGender = event.target.value;
  }

  onBloodGroupSelected(event) {
    this.ceBloodGroup = event.target.value;
  }

  onSchoolSelected(event) {
    this.ceSchoolId = event.target.value;
    this.staff = [];
    this.getStaffBySchool(this.ceSchoolId);
  }

  checkIfStaffNameIsEmpty() {
    if (this.ceStaffName.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfStaffRoleIsEmpty() {
    if (this.ceStaffRoleId === '') {
      return true;
    } else {
      return false;
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onRoleSelected(event) {
    this.ceStaffRoleId = event.target.value;
  }

  // Popup
  showCreateStaffPopup() {
    this.popupTitle = 'Create Student';
    this.ceStaffName = '';
    this.ceDob = '';
    this.ceGender = '';
    this.ceBloodGroup = '';
    this.cestatus = true;
    this.ceGender = 'disabled';
    this.ceBloodGroup = 'disabled';
    this.ceStaffRoleId = '';
    this.updateFlag = false;
    this.getRoles();
    this.basicModal.show();
  }

  editStaffPopup(staffvar: staff) {
    this.popupTitle = 'Edit Student';
    this.ceStaffName = staffvar.name;
    this.ceDob = staffvar.dob;
    this.ceGender = staffvar.gender;
    this.ceBloodGroup = staffvar.bloodGroup;
    this.cestatus = staffvar.status;
    this.ceStaffId = staffvar.id;
    this.updateFlag = true;
    this.getRoles();
    this.basicModal.show();
    this.getRolesByStaffId();
  }

  removeStaffPopup(staffvar: staff) {
    this.ceStaffId = staffvar.id;
    this.conformationModal.show();
  }

  // API
  getcities() {
    this.cityservice.getcities('Bearer ' + this.cookie.get('token')).subscribe(
      (result) => {
        if (result.indicator === 'success') {
          this.cities = result.city;
        }
      },
      (err) => {}
    );
  }

  getSchools(cityId: number) {
    this.schoolService
      .getSchools('Bearer ' + this.cookie.get('token'), cityId)
      .subscribe(
        (result) => {
          if (result.indicator === 'success') {
            this.schools = result.school;
            this.staff = [];
          }
        },
        (err) => {}
      );
  }

  getStaffBySchool(schoolId: number) {
    this.staffService
      .getStaffBySchool('Bearer ' + this.cookie.get('token'), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === 'success') {
            this.staff = result.staff;
          } else {
            this.staff = [];
          }
        },
        (err) => {}
      );
  }

  getRoles() {
    this.userRolesService
      .getRoles('Bearer ' + this.cookie.get('token'))
      .subscribe(
        (result) => {
          if (result.indicator === 'success') {
            this.roles = result.roles;
          }
        },
        (err) => {}
      );
  }

  getRolesByStaffId() {
    this.userRolesService
      .getRolesByStaffId(
        'Bearer ' + this.cookie.get('token'),
        this.ceSchoolId,
        this.ceStaffId
      )
      .subscribe(
        (result) => {
          if (result.indicator === 'success') {
            this.ceStaffRoleId = result.roles.role;
          }
        },
        (err) => {}
      );
  }

  deleteStaff() {
    this.conformationModal.hide();
    this.staffService
      .deleteStaff(
        'Bearer ' + this.cookie.get('token'),
        this.ceSchoolId,
        this.ceStaffId
      )
      .subscribe(
        (result) => {
          if (result.indicator === 'success') {
            this.getStaffBySchool(this.ceSchoolId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }

  add() {
    if (!this.checkIfStaffNameIsEmpty() && !this.checkIfStaffRoleIsEmpty()) {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.staffService
          .updateStaff(
            'Bearer ' + this.cookie.get('token'),
            this.ceSchoolId,
            this.ceStaffRoleId,
            {
              id: this.ceStaffId,
              name: this.ceStaffName,
              gender: this.ceGender,
              dob: this.ceDob,
              address: this.ceStaffAddress,
              bloodGroup: this.ceBloodGroup,
              qualification: this.ceStaffQualification,
              phoneNo: this.cePhone,
              email: this.ceStaffEmail,
              status: this.cestatus,
            }
          )
          .subscribe(
            (result) => {
              if (result.indicator === 'success') {
                this.getStaffBySchool(this.ceSchoolId);
                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.basicModal.hide();
        this.staffService
          .cerateStaff(
            'Bearer ' + this.cookie.get('token'),
            this.ceSchoolId,
            this.ceStaffRoleId,
            {
              id: 0,
              name: this.ceStaffName,
              gender: this.ceGender,
              dob: this.ceDob,
              address: this.ceStaffAddress,
              bloodGroup: this.ceBloodGroup,
              qualification: this.ceStaffQualification,
              phoneNo: this.cePhone,
              email: this.ceStaffEmail,
              status: this.cestatus,
            }
          )
          .subscribe(
            (result) => {
              if (result.indicator === 'success') {
                // Reload Staff
                this.getStaffBySchool(this.ceSchoolId);
                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      }
    } else {
      this.toast.error('Please fill all the mandatory fields');
    }
  }
}
