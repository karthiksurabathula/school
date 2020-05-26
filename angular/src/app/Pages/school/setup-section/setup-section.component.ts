import { SectionService } from "src/app/service/api/school/section/section.service";
import { Component, OnInit, ViewChild } from "@angular/core";
import { ModalDirective, ToastService } from "ng-uikit-pro-standard";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { section } from "src/app/model/school/sectionResponse";
import { GlobalService } from "src/app/service/global/global.service";

@Component({
  selector: "app-setup-section",
  templateUrl: "./setup-section.component.html",
  styleUrls: ["./setup-section.component.css"],
})
export class SetupSectionComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  role: string;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  sections: Array<section>;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;
  ceSectionName = "";
  cestatus: boolean;
  updateFlag: boolean;

  popupTitle: string;
  searchText: string;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private sectionService: SectionService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.ceClassId = 0;
    this.globalService.showNavBar();

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
        this.getClasses(this.ceSchoolId);
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  changeStatus() {
    this.cestatus = !this.cestatus;
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.sections = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.sections = [];
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.sections = [];
    this.ceClassId = event.target.value;
    this.getSections(this.ceSchoolId, this.ceClassId);
  }

  // Popup
  showCreateClassPopup() {
    this.updateFlag = false;
    this.ceSectionId = 0;
    this.ceSectionName = "";
    this.cestatus = true;
    this.popupTitle = "Create Section";
    this.basicModal.show();
  }

  editClassPopup(sectionObj: section) {
    this.updateFlag = true;
    this.ceSectionId = sectionObj.id;
    this.ceSectionName = sectionObj.sectionName;
    this.cestatus = sectionObj.status;
    this.popupTitle = "Edit Section";
    this.basicModal.show();
  }

  removeConformationPopup(sectionObj: section) {
    this.ceSectionId = sectionObj.id;
    this.conformationModal.show();
  }

  // Input Validation
  checkIfSectionNameIsEmpty() {
    if (this.ceSectionName.length > 0) {
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

  add() {
    if (!this.checkIfSectionNameIsEmpty()) {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.sectionService
          .updateSection(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.ceSectionId,
              sectionName: this.ceSectionName,
              status: this.cestatus,
            },
            this.ceSchoolId,
            this.ceClassId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.getSections(this.ceSchoolId, this.ceClassId);
                this.ceSectionId = 0;
                this.ceSectionName = "";
                this.cestatus = true;
                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.basicModal.hide();
        this.sectionService
          .cerateSection(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.ceSectionId,
              sectionName: this.ceSectionName,
              status: this.cestatus,
            },
            this.ceSchoolId,
            this.ceClassId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.getSections(this.ceSchoolId, this.ceClassId);
                this.ceSectionId = 0;
                this.ceSectionName = "";
                this.cestatus = true;
                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      }
    } else {
      this.toast.error("Please fill all the mandatory fields");
    }
  }

  remove() {
    this.conformationModal.hide();
    this.sectionService
      .removeSection(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceSectionId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getSections(this.ceSchoolId, this.ceClassId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }
}
