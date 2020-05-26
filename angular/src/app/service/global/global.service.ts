import { Injectable, OnInit } from "@angular/core";
import { BehaviorSubject } from "rxjs/internal/BehaviorSubject";
import { CookieService } from "ngx-cookie-service";
import { AppStatusService } from "../api/school/appStatus/appStatus.service";

@Injectable({
  providedIn: "root",
})
export class GlobalService {
  private loadingSpinner = new BehaviorSubject<boolean>(false);
  loadingSpinner_Cast = this.loadingSpinner.asObservable();

  private navBar = new BehaviorSubject<boolean>(false);
  navBar_Cast = this.navBar.asObservable();

  role: string;
  ceSchoolId: number;

  constructor(
    private cookie: CookieService,
    private appStatusService: AppStatusService
  ) {}

  showLoadingSpinner() {
    this.loadingSpinner.next(true);
  }

  hideLoadingSpinner() {
    this.loadingSpinner.next(false);
  }

  showNavBar() {
    if (this.cookie.get("role") || this.cookie.get("schoolId")) {
      this.role = this.cookie.get("role");
      this.ceSchoolId = Number(this.cookie.get("schoolId"));

      if (!(this.role === "SUPERUSER")) {
        this.checkStatus(this.ceSchoolId);
      } else {
        this.checkStatusAdmin();
      }
    }
    this.navBar.next(true);
  }

  hideNavBar() {
    this.navBar.next(false);
  }

  checkStatus(schoolId: number) {
    this.appStatusService
      .appStatus("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
          }
        },
        (err) => {}
      );
  }

  checkStatusAdmin() {
    this.appStatusService
      .appStatusAdmin("Bearer " + this.cookie.get("token"))
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
          }
        },
        (err) => {}
      );
  }
}
