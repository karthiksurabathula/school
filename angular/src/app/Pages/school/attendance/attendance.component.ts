import { section } from "src/app/model/school/sectionResponse";
import { Component, OnInit, ViewChild } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { Router, ActivatedRoute } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { GlobalService } from "src/app/service/global/global.service";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { AttendanceService } from "src/app/service/api/school/attendance/attendance.service";
import { attendance } from "src/app/model/school/attendanceResponse";

@Component({
  selector: "app-attendance",
  templateUrl: "./attendance.component.html",
  styleUrls: ["./attendance.component.css"],
})
export class AttendanceComponent implements OnInit {
  
  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private globalService: GlobalService,
    private sectionService: SectionService,
    private attendanceService: AttendanceService
  ) {}

  role: string;
  searchText: string;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  sections: Array<section>;
  attendanceResp: Array<attendance>;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;
  ceDate: string;
  ceExamName: string;

  ngOnInit() {
    this.ceSectionId = 0;
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
          this.getClasses(this.ceSchoolId);
        }
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.sections = [];
    this.attendanceResp = [];
    this.ceDate = null;
    this.ceSectionId = 0;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.sections = [];
    this.attendanceResp = [];
    this.ceDate = null;
    this.ceSectionId = 0;
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.sections = [];
    this.attendanceResp = [];
    this.ceDate = null;
    this.ceSectionId = 0;
    this.ceClassId = event.target.value;
    this.getSections(this.ceSchoolId, this.ceClassId);
  }

  onSectionSelected(event) {
    this.attendanceResp = [];
    this.ceDate = null;
    this.ceSectionId = event.target.value;
    this.getAttendance(this.ceSchoolId, this.ceClassId, this.ceSectionId);
  }

  save() {
    this.saveAttendance(
      this.ceSchoolId,
      this.ceClassId,
      this.ceSectionId,
      this.attendanceResp
    );
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

  getClasses(schoolId: number) {
    this.classService
      .getClasses("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.classes = result.class;
          }
        },
        (err) => {}
      );
  }

  getSections(schoolId: number, classId: number) {
    this.sectionService
      .getSections("Bearer " + this.cookie.get("token"), schoolId, classId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.sections = result.section;
          }
        },
        (err) => {}
      );
  }

  getAttendance(schoolId: number, classId: number, sectionId: number) {
    this.attendanceService
      .getAttendance(
        "Bearer " + this.cookie.get("token"),
        schoolId,
        classId,
        sectionId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.attendanceResp = result.data;
            this.ceDate = result.data[0].day.date;
          }
        },
        (err) => {}
      );
  }

  saveAttendance(
    schoolId: number,
    classId: number,
    sectionId: number,
    attendance: Array<attendance>
  ) {
    console.log(this.attendanceResp);
    this.attendanceService
      .saveAttendance(
        "Bearer " + this.cookie.get("token"),
        schoolId,
        classId,
        sectionId,
        attendance
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getAttendance(this.ceSchoolId, this.ceClassId, this.ceSectionId);
          }
        },
        (err) => {}
      );
  }
}
