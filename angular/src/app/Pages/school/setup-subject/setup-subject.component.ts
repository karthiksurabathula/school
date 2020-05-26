import { Component, OnInit, ViewChild } from "@angular/core";
import { ModalDirective, ToastService } from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { GlobalService } from "src/app/service/global/global.service";
import { SubjectService } from "src/app/service/api/school/subject/subject.service";
import { subject } from "src/app/model/school/subjectResponse";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";

@Component({
  selector: "app-setup-subject",
  templateUrl: "./setup-subject.component.html",
  styleUrls: ["./setup-subject.component.css"],
})
export class SetupSubjectComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  updateFlag: boolean;
  role: string;
  searchText: string;

  ceCityId: number;
  ceSchoolId: number;
  ceSubjectId: number;
  ceSubjectName: string = "";
  popupTitle: string;

  cities: Array<city>;
  schools: Array<school>;
  subjects: Array<subject>;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private globalService: GlobalService,
    private subjectService: SubjectService,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    this.ceSchoolId = 0;
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
        this.getSubjects(this.ceSchoolId);
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  //Validations
  checkIfSubjectNameIsEmpty() {
    if (this.ceSubjectName.length > 0) {
      return false;
    } else {
      return true;
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
    this.getSubjects(this.ceSchoolId);
  }

  //Popup
  showCreateSubjectPopup() {
    this.updateFlag = false;
    this.ceSubjectId = 0;
    this.ceSubjectName = "";
    this.popupTitle = "Create Subject";
    this.basicModal.show();
  }

  editSubjectPopup(subjectObj: subject) {
    this.updateFlag = true;
    this.ceSubjectId = subjectObj.id;
    this.ceSubjectName = subjectObj.subjectName;
    this.popupTitle = "Edit Subject";
    this.basicModal.show();
  }

  removeConformationPopup(subjectObj: subject) {
    this.ceSubjectId = subjectObj.id;
    this.conformationModal.show();
  }

  //API
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

  add() {
    if (!this.checkIfSubjectNameIsEmpty()) {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.subjectService
          .updateSubject(
            "Bearer " + this.cookie.get("token"),
            { id: this.ceSubjectId, subjectName: this.ceSubjectName },
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.ceSubjectId = 0;
                this.ceSubjectName = "";
                this.basicModal.hide();
                this.getSubjects(this.ceSchoolId);
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.basicModal.hide();
        this.subjectService
          .cerateSubject(
            "Bearer " + this.cookie.get("token"),
            { id: this.ceSubjectId, subjectName: this.ceSubjectName },
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.ceSubjectId = 0;
                this.ceSubjectName = "";
                this.basicModal.hide();
                this.getSubjects(this.ceSchoolId);
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

  getSubjects(schoolId: number) {
    this.subjectService
      .getSubjects("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.subjects = result.subject;
          }
        },
        (err) => {}
      );
  }

  remove() {
    this.conformationModal.hide();
    this.subjectService
      .removeSubject(
        "Bearer " + this.cookie.get("token"),
        this.ceSubjectId,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getSubjects(this.ceSchoolId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }
}
