import { Component, OnInit, ViewChild } from "@angular/core";
import { examPartResp } from "src/app/model/school/examResponse";
import { ModalDirective } from "ng-uikit-pro-standard";
import { CookieService } from "ngx-cookie-service";
import { Router, ActivatedRoute } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { ExamService } from "src/app/service/api/school/exam/exam.service";
import { GlobalService } from "src/app/service/global/global.service";
import { MarksService } from "src/app/service/api/school/marks/marks.service";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";

import { marksResp } from "src/app/model/school/marksResponse";
import { marksTracker } from "src/app/model/school/marksTrackerResponse";

@Component({
  selector: "app-marks-student",
  templateUrl: "./marks-student.component.html",
  styleUrls: ["./marks-student.component.css"],
})
export class MarksStudentComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;

  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private examService: ExamService,
    private globalService: GlobalService,
    private marksSerivice: MarksService,
    private route: ActivatedRoute
  ) {}

  role: string;
  searchText: string;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  examRep: Array<examPartResp>;
  marksTrackerResp: Array<marksTracker>;
  studentMarks: Array<marksResp>;

  ceCityId: number;
  ceSchoolId: number;
  ceExamId: number;
  ceClassId: number;
  ceStudentId: number;

  ceExamName: string = '';
  ceExamScope: string;
  ceSectionName: string;
  ceClassName: string;
  ceExamDate: string;
  ceExamMaxMarks: number;

  tempExamPartResp: examPartResp;

  ngOnInit() {
    this.role = this.cookie.get("role");
    this.globalService.showNavBar();

    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      if (this.cookie.get("role") === "SUPERUSER") {
        this.getcities();
      } else {
        if (this.cookie.get("role") !== "STUDENT") {
          this.ceSchoolId = Number(this.cookie.get("schoolId"));
          this.getClasses(this.ceSchoolId);
        }
      }
    } else {
      this.router.navigate(["/login"]);
    }

    this.route.paramMap.subscribe((params) => {
      this.route.queryParams.subscribe((params) => {
        if (
          !Number.isNaN(+params["schoolId"]) &&
          !Number.isNaN(+params["classId"]) &&
          !Number.isNaN(+params["examId"])
        ) {
          this.ceSchoolId = +params["schoolId"];
          this.ceClassId = +params["classId"];
          this.ceExamId = +params["examId"];
          this.getMarksByClass();
        }
      });
    });
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.examRep = [];
    this.ceExamName = '';
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.examRep = [];
    this.ceExamName = '';
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.examRep = [];
    this.ceExamName = '';
    this.ceClassId = event.target.value;
    this.getExams(this.ceSchoolId);
  }

  onExamSelected(event) {
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
    this.getMarksByClass();
  }

  showMarksOfStudent(marksTracker: marksTracker) {
    this.ceStudentId = marksTracker.student.id;
    this.getMarksByStudent();
    this.basicModal.show();
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

  getMarksByClass() {
    this.marksSerivice
      .getMarksByClass(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceExamId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.marksTrackerResp = result.data;
          } else {
            this.marksTrackerResp = [];
          }
        },
        () => {}
      );
  }

  getMarksByStudent() {
    this.marksSerivice
      .getMarksByStudent(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceExamId,
        this.ceStudentId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.studentMarks = result.data;
          } else {
            this.studentMarks = [];
          }
        },
        () => {}
      );
  }
}
