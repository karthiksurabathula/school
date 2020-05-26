import { ExamNotesService } from "src/app/service/api/school/examNotes/examNotes.service";
import { examPartResp } from "src/app/model/school/examResponse";
import { Component, OnInit } from "@angular/core";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { CookieService } from "ngx-cookie-service";
import { ToastService, IMyOptions } from "ng-uikit-pro-standard";
import { Router, ActivatedRoute } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { ExamService } from "src/app/service/api/school/exam/exam.service";
import { ExamTimetableService } from "src/app/service/api/school/examTimetable/exam-timetable.service";
import { schedule, examtimeTableRequest } from "src/app/model/school/examTimetable";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { classs } from "src/app/model/school/classResponse";
import { examNotesResponse1, examNotes } from "src/app/model/school/examNotesResponse";

@Component({
  selector: "app-exam-timetable",
  templateUrl: "./exam-timetable.component.html",
  styleUrls: ["./exam-timetable.component.css"],
})
export class ExamTimetableComponent implements OnInit {
  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private examService: ExamService,
    private globalService: GlobalService,
    private examTimetableService: ExamTimetableService,
    private classService: ClassService,
    private examNotesService: ExamNotesService,
    private route: ActivatedRoute
  ) {}

  hiddenByDirectLink: boolean;

  role: string;
  updateFlag: boolean;
  testScope: string;
  searchText: string;
  note = "";
  examNotes: examNotes;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  examRep: Array<examPartResp>;
  schedule: Array<schedule>;
  tempExamPartResp: examPartResp;
  examtimeTableRequest: examtimeTableRequest;

  ceCityId: number;
  ceSchoolId: number;
  ceExamId: number;
  ceClassId: number;
  ceExamName: string;
  ceExamScope: string;
  ceSectionName: string;
  ceClassName: string;

  hiddenNote: boolean;
  hiddenClass: boolean;

  public myDatePickerOptions: IMyOptions = {
    minYear: new Date().getFullYear() - 150,
    maxYear: new Date().getFullYear(),
    closeAfterSelect: true,
    dateFormat: "dd-mm-yyyy",
  };

  ngOnInit() {
    if (
      this.cookie.check("token") ||
      this.cookie.get("role") ||
      this.cookie.get("schoolId")
    ) {
      this.role = this.cookie.get("role");
      if (this.cookie.get("role") === "SUPERUSER") {
        this.hiddenByDirectLink = true;
        this.hiddenClass = false;
        this.getcities();
      } else {
        this.hiddenClass = false;
        this.hiddenByDirectLink = false;
        this.ceSchoolId = Number(this.cookie.get("schoolId"));
        if (!(this.cookie.get("role") === "STUDENT")) {
          this.getExams(this.ceSchoolId);
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
          this.hiddenByDirectLink = false;
          this.getExamTimeTable();
        } else if (
          !Number.isNaN(+params["schoolId"]) &&
          !Number.isNaN(+params["examId"])
        ) {
          this.ceSchoolId = +params["schoolId"];
          this.ceExamId = +params["examId"];
          this.hiddenByDirectLink = true;
          this.hiddenClass = true;
          this.getClasses(this.ceSchoolId);
        } else {
          this.hiddenByDirectLink = true;
        }
      });
    });

    this.globalService.showNavBar();
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.schedule = [];
    this.examRep = [];
    this.hiddenNote = false;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.ceSchoolId = event.target.value;
    this.schedule = [];
    this.examRep = [];
    this.hiddenNote = false;
    this.getExams(this.ceSchoolId);
  }

  onExamSelected(event) {
    this.hiddenNote = false;
    this.schedule = [];
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
    this.ceClassId = this.tempExamPartResp.classId;
    this.testScope = this.tempExamPartResp.scope;
    if (this.tempExamPartResp.scope === "School") {
      this.getClasses(this.ceSchoolId);
      this.hiddenClass = true;
    } else if (this.tempExamPartResp.scope === "Class") {
      this.hiddenClass = false;
      this.getExamTimeTable();
    } else if (this.tempExamPartResp.scope === "Section") {
      this.hiddenClass = false;
      this.getExamTimeTable();
    }
  }

  onClassSelected(event) {
    this.ceClassId = event.target.value;
    this.getExamTimeTable();
  }

  saveEamTimeTable() {
    console.log(this.schedule);
    this.saveExamTimeTable();
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
      .getExamsBySchool("Bearer " + this.cookie.get("token"), schoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examRep = result.exams;
          }
        },
        () => {}
      );
  }

  getExamTimeTable() {
    this.examTimetableService
      .getExamTimeTable(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceExamId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.schedule = result.schedule;
            this.ceExamName = result.exam.name;
            this.ceExamScope = result.exam.scope;
            this.hiddenNote = true;
            this.testScope = result.exam.scope;
          }
        },
        () => {}
      );
    this.getExamNote();
  }

  saveExamTimeTable() {
    this.examtimeTableRequest = new examtimeTableRequest();
    this.examtimeTableRequest.schedule = this.schedule;
    this.examTimetableService
      .saveExamTimeTable(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceExamId,
        this.examtimeTableRequest
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getExamTimeTable();
          }
        },
        () => {}
      );
    this.saveExamNote();
  }

  saveExamNote() {
    console.log(this.examNotes);
    this.examNotesService
      .saveExamNotes(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceExamId,
        this.examNotes
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examNotes = result.examNotes;
            this.note = this.examNotes.note;
          }
        },
        (err) => {}
      );
  }

  getExamNote() {
    this.examNotesService
      .getExamNotes(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceExamId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.examNotes = result.examNotes;
            this.note = this.examNotes.note;
          }
        },
        (err) => {}
      );
  }
}
