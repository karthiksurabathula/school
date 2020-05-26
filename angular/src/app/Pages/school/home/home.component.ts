import { AnalyticsService } from "./../../../service/api/school/analytics/analytics.service";
import { Component, OnInit } from "@angular/core";
import { GlobalService } from "src/app/service/global/global.service";
import { Router } from "@angular/router";
import {
  analyticsResponse,
  analytics,
} from "src/app/model/school/analyticsResponse";
import { CookieService } from "ngx-cookie-service";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.css"],
})
export class HomeComponent implements OnInit {
  constructor(
    private globalService: GlobalService,
    private router: Router,
    private analyticsService: AnalyticsService,
    private cookie: CookieService
  ) {}

  analytics: Array<analytics>;

  movie: any[];

  ngOnInit() {
    this.globalService.showNavBar();

    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      if (this.cookie.get("role") === "SUPERUSER") {
        this.getAnalytics(0);
      } else {
        this.getAnalytics(+this.cookie.get("schoolId"));
      }
    }

    this.populate();
  }

  populate() {
    this.movie = [
      {
        descriptor: "City",
        count: 5,
        image: "location.png",
        refer: "/city",
      },
      {
        descriptor: "school",
        count: 5,
        image: "school.png",
        refer: "/school",
      },
      {
        descriptor: "student",
        count: 5,
        image: "student.png",
        refer: "/student",
      },
      {
        descriptor: "staff",
        count: 5,
        image: "teacher.png",
        refer: "/staff",
      },
      {
        descriptor: "absentees",
        count: 5,
        image: "sick.png",
        refer: "/attendance-tracker",
      },
      {
        descriptor: "test",
        count: 5,
        image: "test.png",
        refer: "/exam",
      },
      {
        descriptor: "timetable",
        count: 5,
        image: "clock.png",
        refer: "/clock",
      },
      {
        descriptor: "announcement",
        count: 5,
        image: "bell.png",
        refer: "/timetable",
      },
    ];
  }

  route(item: analytics) {
    
    this.router.navigate([item.refer]);
  }

  getAnalytics(schoolId: number) {
    this.analyticsService
      .getAnalytics("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.analytics = result.analytics;
          }
        },
        (err) => {}
      );
  }
}
