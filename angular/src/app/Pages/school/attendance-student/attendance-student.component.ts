import { AttendanceService } from "src/app/service/api/school/attendance/attendance.service";
import { Component, OnInit } from "@angular/core";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { CookieService } from "ngx-cookie-service";
import { ToastService } from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { attendance } from "src/app/model/school/attendanceResponse";

@Component({
  selector: "app-attendance-student",
  templateUrl: "./attendance-student.component.html",
  styleUrls: ["./attendance-student.component.css"],
})
export class AttendanceStudentComponent implements OnInit {
  role: string;
  searchText: string;
  ceStudentId = "";

  cities: Array<city>;
  schools: Array<school>;
  data: Array<attendance>;

  ceStudentName: string;
  ceClassName: string;
  ceSectionName: string;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private globalService: GlobalService,
    private attendanceService: AttendanceService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    this.ceStudentName = "";
    this.ceSchoolId = 0;
    this.schools = [];
    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      this.role = this.cookie.get("role");
      if (this.cookie.get("role") === "SUPERUSER") {
        this.getcities();
      } else {
        this.ceSchoolId = Number(this.cookie.get("schoolId"));
        if (this.cookie.get("role") === "STUDENT") {
          this.ceClassId = Number(this.cookie.get("classId"));
          this.ceSectionId = Number(this.cookie.get("sectionId"));
          this.getAbsentiesByStudentId();
        }
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.ceStudentName = "";
    this.ceSchoolId = 0;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.ceStudentName = "";
    this.ceSchoolId = event.target.value;
  }

  checkIfStudentIdIsEmpty() {
    if (this.ceStudentId.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  // API
  getcities() {
    this.cityservice.getcities("Bearer " + this.cookie.get("token")).subscribe(
      (result) => {
        if (result.indicator === "success") {
          this.cities = result.city;
        }
      },
      (err) => {}
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
        (err) => {}
      );
  }

  getAbsentiesByStudent() {
    this.ceStudentName = "";
    this.attendanceService
      .absenteesByStudentId(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceStudentId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.data = result.data;
            if (result.data.length > 0) {
              this.ceStudentName = result.data[0].student.name;
              this.ceClassName = result.data[0].class.className;
              this.ceSectionName = result.data[0].section.sectionName;
            }
          } else {
            this.data = [];
          }
        },
        (err) => {}
      );
  }

  getAbsentiesByStudentId() {
    this.ceStudentName = "";
    this.attendanceService
      .absenteesByStudentIdNum(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        Number(this.cookie.get("studentId"))
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.data = result.data;
            if (result.data.length > 0) {
              this.ceStudentName = result.data[0].student.name;
              this.ceClassName = result.data[0].class.className;
              this.ceSectionName = result.data[0].section.sectionName;
            }
          } else {
            this.data = [];
          }
        },
        (err) => {}
      );
  }
}
