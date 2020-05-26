import { timeTableRequest } from "src/app/model/school/timtableResponse";
import { InitTrackerService } from "src/app/service/api/school/initTracker/initTracker.service";
import { Component, OnInit } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { ToastService } from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { GlobalService } from "src/app/service/global/global.service";
import { initTracker } from "src/app/model/school/initTrackerResponse";
import { city } from 'src/app/model/school/cityResponse';
import { school } from 'src/app/model/school/schoolResponse';
import { SchoolCityService } from 'src/app/service/api/school/schoolCity/schoolCity.service';
import { SchoolService } from 'src/app/service/api/school/school/school.service';

@Component({
  selector: "app-sample-data",
  templateUrl: "./sample-data.component.html",
  styleUrls: ["./sample-data.component.css"],
})
export class SampleDataComponent implements OnInit {
  city: number = 1;
  school: number = 1;
  classs: number = 2;
  section: number = 2;
  subject: number = 8;
  staff: number = 8;
  student: number = 30;
  period: number = 10;
  disabled: boolean = false;

  schoolId: number;
  ceCityId: number;

  cities: Array<city>;
  schools: Array<school>;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private globalService: GlobalService,
    private initTrackerService: InitTrackerService,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();

    if (this.cookie.check("token") || this.cookie.get("role")) {
      if (this.cookie.get("role") !== "SUPERUSER") {
        this.router.navigate(["/home"]);
      }
    } else {
      this.router.navigate(["/login"]);
    }
    this.checkStatus();
  }

  checkStatus() {
    this.initTrackerService
      .checkDataCreationStatus("Bearer " + this.cookie.get("token"))
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.disabled = result.initTracker.status;
            if(!this.disabled) {
              this. getcities();
            }
          }
        },
        (err) => {}
      );
  }

  createSampleData() {
    this.initTrackerService
      .createSampleData(
        "Bearer " + this.cookie.get("token"),
        this.city,
        this.school,
        this.classs,
        this.section,
        this.subject,
        this.staff,
        this.student,
        this.period
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.disabled = result.initTracker.status;
          } else {
          }
        },
        (err) => {}
      );
  }

  createSampleDataDaily() {
    this.initTrackerService
      .createSampleDailyTasksData(
        "Bearer " + this.cookie.get("token"),
        this.schoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.disabled = result.initTracker.status;
          } else {
          }
        },
        (err) => {}
      );
  }

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

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.schoolId = event.target.value;
  }
}
