import { Component, OnInit, ViewChild } from "@angular/core";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { GlobalService } from "src/app/service/global/global.service";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { Router } from "@angular/router";
import { ToastService, ModalDirective } from "ng-uikit-pro-standard";
import { CookieService } from "ngx-cookie-service";
import { SubjectService } from "src/app/service/api/school/subject/subject.service";
import { subject } from "src/app/model/school/subjectResponse";
import { subjectClassMap } from "src/app/model/school/subjectClassMap";
import { StaffService } from "src/app/service/api/school/staff/staff.service";
import { staff } from "src/app/model/school/staffResponse";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { section } from "src/app/model/school/sectionResponse";

@Component({
  selector: "app-add-subject-to-class",
  templateUrl: "./add-subject-to-class.component.html",
  styleUrls: ["./add-subject-to-class.component.css"],
})
export class AddSubjectToClassComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;
  @ViewChild("basicModalSubjectStaffMap", { static: true })
  basicModalSubjectStaffMap: ModalDirective;

  constructor(
    private globalService: GlobalService,
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private subjectService: SubjectService,
    private sectionService: SectionService,
    private staffService: StaffService
  ) {}

  role: string;
  searchText: string;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  staff: Array<staff>;
  sections: Array<section>;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: any;
  cePopupSubjectId: any;
  cePopupStaffId: any;
  ceSubjectClassMapId: number;
  ceSectionSelected: any;

  popupTitle: string;

  subjects: Array<subject>;
  subjectClassMap: Array<subjectClassMap>;

  subjectOptionalCheckBox = false;
  updateFlag = false;
  disabledCheck = "";

  ngOnInit() {
    this.globalService.showNavBar();
    this.ceClassId = 0;

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

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.subjectClassMap = [];
    this.ceClassId = 0;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.subjectClassMap = [];
    this.ceClassId = 0;
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.ceClassId = event.target.value;
    this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
  }

  onStaffMapPopup(event) {
    this.cePopupStaffId = event.target.value;
  }

  onClassMapPopup(event) {
    this.cePopupSubjectId = event.target.value;
  }

  //Pop Up
  mapSubjectPopup() {
    this.updateFlag = false;
    this.popupTitle = "Add Subject";
    this.cePopupSubjectId = "disabled";
    this.subjectOptionalCheckBox = false;
    this.ceSubjectClassMapId = 0;
    this.getSubjects();
    this.basicModal.show();
  }

  //Pop Up
  mapStaffPopup(subjectClass: subjectClassMap) {
    this.updateFlag = false;
    this.ceSectionSelected = "disabled";
    this.cePopupStaffId = "disabled";
    this.popupTitle = "Assign Teacher";
    //this.cePopupStaffId = 'disabled';
    this.cePopupSubjectId = subjectClass.subjectId;
    this.getStaffBySchool(this.ceSchoolId);
    this.getSections(this.ceSchoolId, this.ceClassId);
    this.basicModalSubjectStaffMap.show();
  }

  onSectionSelected(event) {
    this.ceSectionId = event.target.value;
    this.getSubjectStaffMap();
  }

  editMapSubjectPopup(subjectClass: subjectClassMap) {
    this.updateFlag = true;
    this.popupTitle = "Edit Subject";
    this.subjectOptionalCheckBox = subjectClass.optional;
    this.subjects = [
      { id: subjectClass.subjectId, subjectName: subjectClass.subjectName },
    ];
    this.cePopupSubjectId = subjectClass.subjectId;
    this.ceSchoolId = subjectClass.schoolId;
    this.ceClassId = subjectClass.classId;
    this.ceSubjectClassMapId = subjectClass.subjectMapId;
    this.basicModal.show();
  }

  removeMapSubjectPopup(subjectClass: subjectClassMap) {
    this.ceSubjectClassMapId = subjectClass.subjectMapId;
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

  getSubjects() {
    this.subjectService
      .getSubjects("Bearer " + this.cookie.get("token"), this.ceSchoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.subjects = result.subject;
          }
        },
        (err) => {}
      );
  }

  createSubjectClassMap() {
    if (this.cePopupSubjectId === "disabled") {
      this.toast.error("Please Select Subject", "ERROR", { opacity: 1 });
    } else {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.subjectService
          .updateSubjectClassMap("Bearer " + this.cookie.get("token"), {
            schoolId: this.ceSchoolId,
            classId: this.ceClassId,
            subjectId: this.cePopupSubjectId,
            optional: this.subjectOptionalCheckBox,
            subjectName: "",
            subjectMapId: this.ceSubjectClassMapId,
          })
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.basicModal.hide();
                this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.basicModal.hide();
        this.subjectService
          .cerateSubjectClassMap("Bearer " + this.cookie.get("token"), {
            schoolId: this.ceSchoolId,
            classId: this.ceClassId,
            subjectId: this.cePopupSubjectId,
            optional: this.subjectOptionalCheckBox,
            subjectName: "",
            subjectMapId: this.ceSubjectClassMapId,
          })
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.basicModal.hide();
                this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      }
    }
  }

  getSubjectByClass(schoolId: number, calssId: number) {
    this.subjectService
      .getSubjectClassMap(
        "Bearer " + this.cookie.get("token"),
        schoolId,
        calssId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.subjectClassMap = result.subjectClassMap;
          } else if (result.indicator === "fail") {
            this.subjectClassMap = [];
          }
        },
        (err) => {}
      );
  }

  removeMapSubjectConfPopup() {
    this.conformationModal.hide();
    this.subjectService
      .removeSubjectClassMap(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceSubjectClassMapId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }

  getStaffBySchool(schoolId: number) {
    this.staffService
      .getStaffBySchool("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.staff = result.staff;
          }
        },
        (err) => {}
      );
  }

  createSubjectStaffMap() {
    this.basicModalSubjectStaffMap.hide();
    this.subjectService
      .createSubjectStaffMap(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.cePopupSubjectId,
        this.cePopupStaffId,
        this.ceSectionId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.basicModalSubjectStaffMap.hide();
          } else {
            this.basicModalSubjectStaffMap.show();
          }
        },
        (err) => {}
      );
  }

  getSubjectStaffMap() {
    this.subjectService
      .getSubjectStaffMap(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.cePopupSubjectId,
        this.ceSectionId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            console.log(result);
            this.cePopupStaffId = result.staffId;
          }
        },
        (err) => {}
      );
  }
}
