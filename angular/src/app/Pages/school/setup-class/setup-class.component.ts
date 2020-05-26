import { GlobalService } from "src/app/service/global/global.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { Component, OnInit, ViewChild } from "@angular/core";
import {
  ToastService,
  ModalDirective,
  MdbTableDirective,
} from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { CookieService } from "ngx-cookie-service";
import { city } from "src/app/model/school/cityResponse";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";

@Component({
  selector: "app-setup-class",
  templateUrl: "./setup-class.component.html",
  styleUrls: ["./setup-class.component.css"],
})
export class SetupClassComponent implements OnInit {
  // @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceClassName = "";
  cestatus: boolean;
  updateFlag: boolean;

  popupTitle: string;
  searchText: string;
  role: string;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.ceSchoolId = 0;
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
    this.ceCityId = event.target.value;
    this.classes = null;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  // Popup
  showCreateClassPopup() {
    this.updateFlag = false;
    this.ceClassId = 0;
    this.ceClassName = "";
    this.cestatus = true;
    this.popupTitle = "Create Class";
    this.basicModal.show();
  }

  editClassPopup(classObj: classs) {
    this.updateFlag = true;
    this.ceClassId = classObj.id;
    this.ceClassName = classObj.className;
    this.cestatus = classObj.status;
    this.popupTitle = "Edit Class";
    this.basicModal.show();
  }

  removeConformationPopup(classObj: classs) {
    this.ceClassId = classObj.id;
    this.conformationModal.show();
  }

  // Input Validation
  checkIfClassNameIsEmpty() {
    if (this.ceClassName.length > 0) {
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
        } else {
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
          } else {
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

  add() {
    if (!this.checkIfClassNameIsEmpty()) {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.classService
          .updateClass(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.ceClassId,
              className: this.ceClassName,
              status: this.cestatus,
            },
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.getClasses(this.ceSchoolId);
                this.ceClassId = 0;
                this.ceClassName = "";
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
        this.classService
          .cerateClass(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.ceClassId,
              className: this.ceClassName,
              status: this.cestatus,
            },
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.getClasses(this.ceSchoolId);
                this.ceClassId = 0;
                this.ceClassName = "";
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
    this.classService
      .removeClass(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getClasses(this.ceSchoolId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }
}
