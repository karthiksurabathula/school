import { MarksService } from "src/app/service/api/school/marks/marks.service";
import { ExamService } from "src/app/service/api/school/exam/exam.service";
import { Component, OnInit, ViewChild } from "@angular/core";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { section } from "src/app/model/school/sectionResponse";
import { CookieService } from "ngx-cookie-service";
import { ToastService, ModalDirective } from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { GlobalService } from "src/app/service/global/global.service";
import { examPartResp } from "src/app/model/school/examResponse";
import { marksStatus } from "src/app/model/school/marksStatus";

@Component({
  selector: "app-exam",
  templateUrl: "./exam.component.html",
  styleUrls: ["./exam.component.css"],
})
export class ExamComponent implements OnInit {
  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private sectionService: SectionService,
    private globalService: GlobalService,
    private examService: ExamService,
    private marksSerivice: MarksService
  ) {}

  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;
  @ViewChild("marksStatusModal", { static: true })
  marksStatusModal: ModalDirective;

  role: string;
  scopeSelected: string;
  updateFlag: boolean;
  searchText: string;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  sections: Array<section>;
  examRep: Array<examPartResp>;
  scope: Array<any>;
  examMarksStatus: Array<marksStatus>;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;
  ceExamId: number;
  ceExamStatusPending: number;
  ceExamStatusCompleted: number;

  ceExamName = "";
  ceExamDescription = "";
  ceScopeSelected;
  popupTitle: string;

  radio: string;

  ngOnInit() {
    this.ceSchoolId = 0;
    this.globalService.showNavBar();
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
        if (this.cookie.get("role") === "STUDENT") {
          this.ceClassId = Number(this.cookie.get("classId"));
          this.ceSectionId = Number(this.cookie.get("sectionId"));
          this.getExamsBySchoolAndClassSection();
        } else {
          if (this.cookie.get("role") === "TEACHER") {
            this.getExamsByTeacher(this.ceSchoolId);
          } else {
            this.getExams(this.ceSchoolId);
          }
        }
      }
    } else {
      this.router.navigate(["/login"]);
    }

    if (
      this.cookie.get("role") === "SUPERUSER" ||
      this.cookie.get("role") === "ADMIN"
    ) {
      this.scope = [
        { id: "School", label: "School" },
        { id: "Class", label: "Class" },
        { id: "Section", label: "Section" },
      ];
    } else if (this.cookie.get("role") === "TEACHER") {
      this.scope = [
        { id: "Class", label: "Class" },
        { id: "Section", label: "Section" },
      ];
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.sections = [];
    this.examRep = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.sections = [];
    this.examRep = [];
    this.ceSchoolId = event.target.value;
    this.getExams(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.ceClassId = event.target.value;
    if (this.scopeSelected === "Section") {
      this.getSections(this.ceSchoolId, this.ceClassId);
    }
  }

  onScopeSelected(event) {
    this.scopeSelected = event;
    if (event === "Class") {
      this.ceClassId = 0;
      this.getClasses(this.ceSchoolId);
    } else if (event === "Section") {
      this.ceClassId = 0;
      this.ceSectionId = 0;
    }
  }

  // Input Validation
  checkIfExamNameIsEmpty() {
    if (this.ceExamName.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  // Pop Up
  showCreateExamPopup() {
    this.updateFlag = false;
    this.popupTitle = "Create Exam";
    this.ceExamName = "";
    this.ceExamDescription = "";
    this.ceScopeSelected = "disabled";
    this.onScopeSelected("disabled");
    this.ceClassId = 0;
    this.ceSectionId = 0;
    this.basicModal.show();
  }

  editExamPopup(examPartResp: examPartResp) {
    this.updateFlag = true;
    this.popupTitle = "Edit Exam";
    this.ceExamId = examPartResp.exam.id;
    this.ceExamName = examPartResp.examName;
    this.ceExamDescription = examPartResp.exam.description;
    this.ceSchoolId = examPartResp.schoolId;
    this.ceScopeSelected = examPartResp.exam.scope;

    this.onScopeSelected(this.ceScopeSelected);

    this.ceClassId = examPartResp.classId;
    this.ceSectionId = examPartResp.sectionId;
    if (this.ceScopeSelected === "Section") {
      this.getClasses(this.ceSchoolId);
      this.getSections(this.ceSchoolId, this.ceClassId);
    }
    this.basicModal.show();
  }

  examTimetablePopup(examPartResp: examPartResp) {
    this.router.navigate(["/exam-timetable"], {
      queryParams: {
        schoolId: examPartResp.schoolId,
        examId: examPartResp.exam.id,
        classId: this.ceClassId,
      },
    });
  }

  marksStatus(examPartResp: examPartResp) {
    this.ceClassId = examPartResp.classId;
    this.ceExamId = examPartResp.exam.id;
    console.log(this.ceExamStatusCompleted + " " + this.ceExamStatusPending);
    this.getExamTimeTable();
    this.marksStatusModal.show();
  }

  marksByStudent(examPartResp: examPartResp) {
    this.router.navigate(["/marks-student"], {
      queryParams: {
        schoolId: this.ceSchoolId,
        examId: examPartResp.exam.id,
        classId: this.ceClassId,
      },
    });
  }

  add() {
    console.log(this.ceScopeSelected);
    if (this.ceScopeSelected === "School") {
      this.saveExam(this.ceSchoolId, 0, 0);
    } else if (this.ceScopeSelected === "Class") {
      this.saveExam(this.ceSchoolId, this.ceClassId, 0);
    } else if (this.ceScopeSelected === "Section") {
      this.saveExam(this.ceSchoolId, this.ceClassId, this.ceSectionId);
    }
  }

  removeConformationPopup(examPartResp: examPartResp) {
    this.ceExamId = examPartResp.exam.id;
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

  saveExam(schoolId: number, classId: number, sectionId: number) {
    if (this.updateFlag) {
      this.basicModal.hide();
      this.examService
        .updateExam(
          "Bearer " + this.cookie.get("token"),
          schoolId,
          classId,
          sectionId,
          {
            id: this.ceExamId,
            name: this.ceExamName,
            description: this.ceExamDescription,
            scope: this.ceScopeSelected,
          }
        )
        .subscribe(
          (result) => {
            if (result.indicator === "success") {
              this.getExams(this.ceSchoolId);
              this.basicModal.hide();
            } else {
              this.basicModal.show();
            }
          },
          (err) => {}
        );
    } else {
      this.basicModal.hide();
      this.examService
        .saveExam(
          "Bearer " + this.cookie.get("token"),
          schoolId,
          classId,
          sectionId,
          {
            id: 0,
            name: this.ceExamName,
            description: this.ceExamDescription,
            scope: this.ceScopeSelected,
          }
        )
        .subscribe(
          (result) => {
            if (result.indicator === "success") {
              this.getExams(this.ceSchoolId);
              this.basicModal.hide();
            } else {
              this.basicModal.show();
            }
          },
          (err) => {}
        );
    }
  }

  getExams(schoolId: number) {
    this.examService
      .getExamsBySchool("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examRep = result.exams;
          } else {
            this.examRep = [];
          }
        },
        (err) => {}
      );
  }

  getExamsBySchoolAndClassSection() {
    this.examService
      .getExamsBySchoolAndClass(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceSectionId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examRep = result.exams;
          }
        },
        (err) => {}
      );
  }

  remove() {
    this.conformationModal.hide();
    this.examService
      .removeExam(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceExamId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getExams(this.ceSchoolId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }

  getExamTimeTable() {
    this.ceExamStatusCompleted = 0;
    this.ceExamStatusPending = 0;

    this.marksSerivice
      .getMarksStatus(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceExamId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            for (let i = 0; i < result.examMarksStatus.length; i++) {
              if (result.examMarksStatus[i].subjectStatus.completed) {
                this.ceExamStatusCompleted = this.ceExamStatusCompleted + 1;
              }
              this.ceExamStatusPending = result.examMarksStatus.length;
            }
            this.examMarksStatus = result.examMarksStatus;
          }
        },
        () => {}
      );
  }

  getExamsByTeacher(schoolId: number) {
    this.examService
      .getExamsByTeacher("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examRep = result.exams;
          } else {
            this.examRep = [];
          }
        },
        (err) => {}
      );
  }
}
