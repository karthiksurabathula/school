import { PeriodService } from "src/app/service/api/school/period/period.service";
import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import {
  ModalDirective,
  ToastService
} from "ng-uikit-pro-standard";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { period } from "src/app/model/school/periodResponse";

@Component({
  selector: "app-period",
  templateUrl: "./period.component.html",
  styleUrls: ["./period.component.css"],
})
export class PeriodComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  searchText: string;
  role: string;

  startTime = "";
  endTime = "";
  cePeriodDec = "";
  periodId = 0;

  cities: Array<city>;
  schools: Array<school>;
  periods: Array<period>;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;

  popupTitle: string;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private periodService: PeriodService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    this.ceSchoolId = 0;
    this.role = this.cookie.get("role");
    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      if (this.cookie.get("role") === "SUPERUSER") {
        this.getcities();
      } else {
        this.ceSchoolId = Number(this.cookie.get("schoolId"));
        this.getPeriods(this.ceSchoolId);
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.periods = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.periods = [];
    this.ceSchoolId = event.target.value;
    this.getPeriods(this.ceSchoolId);
  }

  onStartTimeChange = (event: string) => {};

  onEndTimeChange = (event: string) => {};

  showCreatePeriodPopup() {
    this.periodId = 0;
    this.startTime = "";
    this.endTime = "";
    this.cePeriodDec = "";
    this.popupTitle = "Add Period";
    this.basicModal.show();
  }

  editPeriodPopup(period: period) {
    this.periodId = period.id;
    this.startTime = period.startTime;
    this.endTime = period.endTime;
    this.cePeriodDec = period.description;
    this.popupTitle = "Edit Period";
    this.basicModal.show();
  }

  removePeriodPopup(period: period) {
    this.periodId = period.id;
    this.conformationModal.show();
  }

  checkIfPeriodDecIsEmpty() {
    if (this.cePeriodDec.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfStartTimeIsEmpty() {
    if (this.startTime.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfEndTimeIsEmpty() {
    if (this.endTime.length > 0) {
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

  add() {
    console.log(this.startTime);
    if (
      !this.checkIfPeriodDecIsEmpty() &&
      !this.checkIfStartTimeIsEmpty() &&
      !this.checkIfEndTimeIsEmpty()
    ) {
      this.basicModal.hide();
      this.periodService
        .ceratePeriod(
          "Bearer " + this.cookie.get("token"),
          {
            id: this.periodId,
            description: this.cePeriodDec,
            startTime: this.startTime,
            startTimeDisp: "",
            endTime: this.endTime,
            endTimeDisp: "",
          },
          this.ceSchoolId
        )
        .subscribe(
          (result) => {
            if (result.indicator === "success") {
              // Get Periods by Schools
              this.getPeriods(this.ceSchoolId);
              this.basicModal.hide();
            } else {
              this.basicModal.show();
            }
          },
          (err) => {}
        );
    } else {
      this.toast.error("Please fill all the fields");
    }
  }

  getPeriods(schoolId: number) {
    this.periodService
      .getPriods("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.periods = result.period;
          } else {
            this.periods = [];
          }
        },
        (err) => {}
      );
  }

  deletePeriod() {
    this.conformationModal.hide();
    this.periodService
      .deletePeriod(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.periodId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getPeriods(this.ceSchoolId);
            this.conformationModal.hide();
          } else if (result.indicator === "fail") {
            this.conformationModal.show();
            this.periods = [];
          }
        },
        (err) => {}
      );
  }
}
