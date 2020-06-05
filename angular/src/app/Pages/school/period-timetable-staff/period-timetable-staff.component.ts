import { Component, OnInit } from "@angular/core";
import { timeTable } from "src/app/model/school/timtableResponse";
import { CookieService } from "ngx-cookie-service";
import { ToastService } from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { TimeTableService } from "src/app/service/api/school/timeTable/timeTable.service";
import { GlobalService } from "src/app/service/global/global.service";

@Component({
  selector: "app-period-timetable-staff",
  templateUrl: "./period-timetable-staff.component.html",
  styleUrls: ["./period-timetable-staff.component.css"],
})
export class PeriodTimetableStaffComponent implements OnInit {
  searchText: string;
  role: string;

  timeTable: Array<timeTable>;
  ceDay: string;
  ceSchoolId: number;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private timeTableService: TimeTableService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();

    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      this.role = this.cookie.get("role");
      this.ceSchoolId = Number(this.cookie.get("schoolId"));
    } else {
      this.router.navigate(["/login"]);
    }

    let d = new Date();
    let weekday = new Array(7);
    weekday[0] = "Sunday";
    weekday[1] = "Monday";
    weekday[2] = "Tuesday";
    weekday[3] = "Wednesday";
    weekday[4] = "Thursday";
    weekday[5] = "Friday";
    weekday[6] = "Saturday";

    this.ceDay =  weekday[d.getDay()];
    this.getTimeTableStaff(this.ceSchoolId, this.ceDay);
  }

  onDaySelected(event) {
    this.ceDay = event.target.value;
    this.getTimeTableStaff(this.ceSchoolId, this.ceDay);
  }

  // API
  getTimeTableStaff(schoolId: number, day: string) {
    this.timeTableService
      .getTimeTableStaff("Bearer " + this.cookie.get("token"), schoolId, day)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.timeTable = result.timeTable;
          } else {
            this.timeTable = [];
          }
        },
        (err) => {}
      );
  }
}
