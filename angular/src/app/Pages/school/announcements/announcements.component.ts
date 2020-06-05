import { announcement } from "src/app/model/school/announcementResponse";
import { Component, OnInit, ViewChild } from "@angular/core";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { AnnouncementService } from "src/app/service/api/school/announcement/announcement.service";
import { ModalDirective } from "ng-uikit-pro-standard";

@Component({
  selector: "app-announcements",
  templateUrl: "./announcements.component.html",
  styleUrls: ["./announcements.component.css"],
})
export class AnnouncementsComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  cities: Array<city>;
  schools: Array<school>;
  update: boolean;

  ceCityId: number;
  ceSchoolId: number;
  role: string;
  searchText: string;
  title: string;
  description: string;
  announcementId: number;

  announcementResp: Array<announcement>;
  announcement: announcement;

  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private announcementService: AnnouncementService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    if (
      this.cookie.check("token") ||
      this.cookie.check("role") ||
      this.cookie.check("schoolId")
    ) {
      this.role = this.cookie.get("role");

      if (this.cookie.get("role") === "SUPERUSER") {
        this.getcities();
      } else {
        this.ceSchoolId = Number(this.cookie.get("schoolId"));
        this.getAnnouncements();
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.ceSchoolId = event.target.value;
    this.getAnnouncements();
  }

  createAnnouncement() {
    this.update = false;
    this.title = "";
    this.description = "";
    this.basicModal.show();
  }

  editAnnouncement(announcement: announcement) {
    this.update = true;
    this.announcement = announcement;
    this.title = announcement.title;
    this.description = announcement.description;
    this.basicModal.show();
  }

  showAnnouncement(announcement: announcement) {
    if (!(this.role === "SUPERUSER" || this.role === "ADMIN")) {
      this.update = true;
      this.announcement = announcement;
      this.title = announcement.title;
      this.description = announcement.description;
      this.basicModal.show();
    }
  }

  setVibility(announcement: announcement) {
    this.announcement = announcement;
    this.announcement.visibilty = !this.announcement.visibilty;
    this.setVisbility();
  }

  remove(announcement: announcement) {
    this.announcement = announcement;
    this.conformationModal.show();
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

  getAnnouncements() {
    this.announcementService
      .getAnnouncement("Bearer " + this.cookie.get("token"), this.ceSchoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.announcementResp = result.announcement;
          } else {
            this.announcementResp = [];
          }
        },
        () => {}
      );
  }

  createAnnouncements() {
    if (!this.update) {
      this.announcement = new announcement();
      this.announcement.id = this.announcement.id;
      this.announcement.title = this.title;
      this.announcement.description = this.description;
    } else {
      this.announcement.title = this.title;
      this.announcement.description = this.description;
    }

    this.basicModal.hide();

    this.announcementService
      .createAnnouncement(
        "Bearer " + this.cookie.get("token"),
        this.announcement,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getAnnouncements();
            this.basicModal.hide();
          } else {
            this.basicModal.show();
          }
        },
        () => {}
      );
  }

  setVisbility() {
    this.announcementService
      .updateAnnouncementVisibility(
        "Bearer " + this.cookie.get("token"),
        this.announcement,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getAnnouncements();
          }
        },
        () => {}
      );
  }

  removeAnnouncement() {
    this.conformationModal.hide();
    this.announcementService
      .removeAnnouncement(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.announcement.id
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getAnnouncements();
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        () => {}
      );
  }
}
