import { MarksService } from "src/app/service/api/school/marks/marks.service";
import { Component, OnInit, ViewChild } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ExamService } from "src/app/service/api/school/exam/exam.service";
import { GlobalService } from "src/app/service/global/global.service";
import { ExamTimetableService } from "src/app/service/api/school/examTimetable/exam-timetable.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { section } from "src/app/model/school/sectionResponse";
import { examPartResp } from "src/app/model/school/examResponse";
import { SubjectService } from "src/app/service/api/school/subject/subject.service";
import { subjectClassMap } from "src/app/model/school/subjectClassMap";
import { marksResp } from "src/app/model/school/marksResponse";
import { FormControl, Validators } from "@angular/forms";
import { ModalDirective } from "ng-uikit-pro-standard";

@Component({
  selector: "app-marks-subject",
  templateUrl: "./marks-subject.component.html",
  styleUrls: ["./marks-subject.component.css"],
})
export class MarksSubjectComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;

  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private examService: ExamService,
    private globalService: GlobalService,
    private sectionService: SectionService,
    private subjectService: SubjectService,
    private marksSerivice: MarksService
  ) {}

  updateFlag: boolean;
  searchText: string;
  role: string;
  examScope: string;
  inputMarks: boolean;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  sections: Array<section>;
  examRep: Array<examPartResp>;
  subjectClassMap: Array<subjectClassMap>;
  marksData: Array<marksResp>;

  ceCityId: number;
  ceSchoolId: number;
  ceExamId: number;
  ceClassId: number;
  ceSectionId: number;
  ceSubjectId: any;

  ceExamName: string;
  ceExamScope: string;
  ceSectionName: string;
  ceClassName: string;
  ceExamDate: string = "";
  ceExamMaxMarks: number;

  tempExamPartResp: examPartResp;

  rateControl: FormControl;

  ngOnInit() {
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
    this.ceSubjectId = 0;
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.sections = [];
    this.examRep = [];
    this.subjectClassMap = [];
    this.marksData = [];
    this.ceSubjectId = 0;
    this.examScope = "";
    this.ceExamDate = "";
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.sections = [];
    this.examRep = [];
    this.subjectClassMap = [];
    this.marksData = [];
    this.ceSubjectId = 0;
    this.examScope = "";
    this.ceExamDate = "";
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.sections = [];
    this.examRep = [];
    this.subjectClassMap = [];
    this.marksData = [];
    this.ceSubjectId = 0;
    this.examScope = "";
    this.ceExamDate = "";
    this.ceClassId = event.target.value;
    this.getSections(this.ceSchoolId, this.ceClassId);
  }

  onSectionSelected(event) {
    this.examRep = [];
    this.subjectClassMap = [];
    this.marksData = [];
    this.ceSubjectId = 0;
    this.examScope = "";
    this.ceExamDate = "";
    this.ceSectionId = event.target.value;
    this.getExams(this.ceSchoolId);
  }

  onExamSelected(event) {
    this.ceSubjectId = 0;
    this.subjectClassMap = [];
    this.marksData = [];
    this.ceExamId = event.target.value;

    for (let i = 0; i < this.examRep.length; i++) {
      if (this.examRep[i].exam.id == this.ceExamId) {
        this.tempExamPartResp = this.examRep[i];
        break;
      }
    }

    this.ceExamName = this.tempExamPartResp.examName;
    this.ceExamScope = this.tempExamPartResp.scope;
    this.ceSectionName = this.tempExamPartResp.sectionName;
    this.ceClassName = this.tempExamPartResp.className;
    this.examScope = this.tempExamPartResp.scope;
    this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
  }

  onSubjectId(event) {
    this.marksData = [];
    this.ceSubjectId = event.target.value;
    this.getMarksBySubject();
  }

  modelChanged(newObj) {
    console.log(newObj);
  }

  saveEamTimeTable() {
    this.saveMarksBySubject();
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

  getExams(schoolId: number) {
    this.examService
      .getExamsBySchoolScope("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examRep = result.exams;
          }
        },
        () => {}
      );
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
          }
        },
        (err) => {}
      );
  }

  getMarksBySubject() {
    this.marksSerivice
      .getMarksBySubject(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceSectionId,
        this.ceExamId,
        this.ceSubjectId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.marksData = result.data;
            this.ceExamDate = result.timetable.date;
            this.ceExamMaxMarks = result.timetable.marks;
            this.disableMarksInput(new Date(this.ceExamDate));
          } else {
            this.marksData = [];
          }
        },
        () => {}
      );
  }

  disableMarksInput(date: Date): Date {
    date.setDate(date.getDate() + 2);
    this.inputMarks = !(new Date() > date);
    return date;
  }

  saveMarksBySubject() {
    this.marksSerivice
      .saveMarksBySubject(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceSectionId,
        this.ceExamId,
        this.ceSubjectId,
        this.marksData
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
          }
        },
        () => {}
      );
  }
}
