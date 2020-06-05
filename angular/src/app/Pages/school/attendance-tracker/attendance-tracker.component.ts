import { StudentService } from "src/app/service/api/school/student/student.service";
import { Component, OnInit, ViewChild } from "@angular/core";
import { ModalDirective } from "ng-uikit-pro-standard";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { AttendanceService } from "src/app/service/api/school/attendance/attendance.service";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { attendance, attendanceResp } from "src/app/model/school/attendanceResponse";

@Component({
  selector: "app-attendance-tracker",
  templateUrl: "./attendance-tracker.component.html",
  styleUrls: ["./attendance-tracker.component.css"],
})
export class AttendanceTrackerComponent implements OnInit {
  @ViewChild("contactDetailsPopup", { static: true })
  contactDetailsPopup: ModalDirective;

  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private globalService: GlobalService,
    private attendanceService: AttendanceService,
    private studentService: StudentService
  ) {}

  role: string;
  searchText: string;
  searchText1: string;

  fatherName: string;
  fatherPhone: string;
  motherName: string;
  motherPhone: string;
  notes: string;

  cities: Array<city>;
  schools: Array<school>;
  attendanceResp: Array<attendance>;
  attendanceRespStatus: Array<attendance>;
  attendance: attendanceResp;

  ceCityId: number;
  ceSchoolId: number;
  ceDate: string;
  initButton = 0;

  ngOnInit() {
    this.ceSchoolId = 0;
    this.role = this.cookie.get("role");
    this.globalService.showNavBar();

    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      if (this.cookie.get("role") === "SUPERUSER") {
        this.getcities();
      } else {
        if (this.cookie.get("role") !== "STUDENT") {
          this.ceSchoolId = Number(this.cookie.get("schoolId"));
          this.workingDayCheck(this.ceSchoolId) ;
        }
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.ceSchoolId = 0;
    this.attendanceResp = [];
    this.ceDate = null;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.attendanceResp = [];
    this.ceDate = null;
    this.ceSchoolId = event.target.value;
    this.workingDayCheck(this.ceSchoolId);
  }

  contactDetails(attendance: attendance) {
    this.attendance = attendance.attendance;
    this.getStudentById(
      this.ceSchoolId,
      attendance.student.id,
      attendance.attendance.note
    );
  }
  saveNote() {
    this.saveAttendanceNote(this.ceSchoolId, this.notes);
  }

  save() {
    this.saveWorkingDay(this.ceSchoolId);
  }

  // API
  getcities() {
    this.cityservice.getcities("Bearer " + this.cookie.get("token")).subscribe(
      (result) => {
        if (result.indicator === "success") {
          this.cities = result.city;
        }
      },
      () => {}
    );
  }

  getSchools(cityId: number) {
    this.schoolService
      .getSchools("Bearer " + this.cookie.get("token"), cityId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.schools = result.school;
          }
        },
        () => {}
      );
  }

  getAbsentees(schoolId: number) {
    this.attendanceService
      .getAbsentees("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.attendanceResp = result.data;
          } else {
            this.attendanceResp = [];
          }
        },
        (err) => {}
      );
  }

  getStudentById(schoolId: number, studentId: number, note: string) {
    this.studentService
      .getStudentById("Bearer " + this.cookie.get("token"), studentId, schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.fatherName = result.student.fatherName;
            this.fatherPhone = result.student.fatherPhoneNo;
            this.motherName = result.student.motherName;
            this.motherPhone = result.student.motherPhoneNo;
            this.notes = note;
            this.contactDetailsPopup.show();
          }
        },
        (err) => {}
      );
  }

  saveAttendanceNote(schoolId: number, note: string) {
    this.attendance.note = note;
    this.contactDetailsPopup.hide();
    this.attendanceService
      .saveAttendanceNote("Bearer " + this.cookie.get("token"), schoolId, this.attendance)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.contactDetailsPopup.hide();
          } else {
            this.contactDetailsPopup.show();
          }
        },
        (err) => {}
      );
  }

  saveWorkingDay(schoolId: number) {
    this.attendanceService
      .saveWorkingDay("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.ceDate = result.day.date;
            this.getAttendanceStatus(this.ceSchoolId);
          }
        },
        (err) => {}
      );
  }

  workingDayCheck(schoolId: number) {
    this.attendanceService
      .workingDayCheck("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.ceDate = result.day.date;
            this.getAttendanceStatus(this.ceSchoolId);
            this.initButton = 0;
          } else {
            this.initButton = 1;
          }
        },
        (err) => {}
      );
  }


  getAttendanceStatus(schoolId: number) {
    this.attendanceService
      .getAttendanceStatus("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.attendanceRespStatus = result.data;
            this.getAbsentees(this.ceSchoolId);
          } else {
            this.attendanceRespStatus = [];
          }
        },
        (err) => {}
      );
  }
}
