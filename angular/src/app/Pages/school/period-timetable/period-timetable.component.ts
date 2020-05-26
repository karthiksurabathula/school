import { Component, OnInit } from "@angular/core";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { period } from "src/app/model/school/periodResponse";
import { subject } from "src/app/model/school/subjectResponse";
import { SubjectService } from "src/app/service/api/school/subject/subject.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { classs } from "src/app/model/school/classResponse";
import { section } from "src/app/model/school/sectionResponse";
import { TimeTableService } from "src/app/service/api/school/timeTable/timeTable.service";
import {
  timeTable,
  timeTableResponse,
  timeTableRequest,
} from "src/app/model/school/timtableResponse";

@Component({
  selector: "app-period-timetable",
  templateUrl: "./period-timetable.component.html",
  styleUrls: ["./period-timetable.component.css"],
})
export class PeriodTimetableComponent implements OnInit {
  searchText: string;
  role: string;

  cities: Array<city>;
  schools: Array<school>;
  periods: Array<period>;
  classes: Array<classs>;
  sections: Array<section>;
  subjects: Array<subject>;
  timeTable: Array<timeTable>;
  timeTableRequest: timeTableRequest;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;
  ceDay: string;
  weekday = new Array(7);
  d = new Date();

  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private timeTableService: TimeTableService,
    private globalService: GlobalService,
    private subjectService: SubjectService,
    private classService: ClassService,
    private sectionService: SectionService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    this.ceDay = "";
    this.ceSectionId = 0;


    this.weekday[0] = "Sunday";
    this.weekday[1] = "Monday";
    this.weekday[2] = "Tuesday";
    this.weekday[3] = "Wednesday";
    this.weekday[4] = "Thursday";
    this.weekday[5] = "Friday";
    this.weekday[6] = "Saturday";


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
        if (this.cookie.get("role") === "STUDENT") {
          this.ceClassId = Number(this.cookie.get("classId"));
          this.ceSectionId = Number(this.cookie.get("sectionId"));

          this.ceDay = this.weekday[this.d.getDay()];
          this.getTimeTable(
            this.ceSchoolId,
            this.ceClassId,
            this.ceSectionId,
            this.ceDay
          );
        } else {
          this.getClasses(this.ceSchoolId);
        }
      }
    } else {
      this.router.navigate(["/login"]);
    }

    
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.sections = [];
    this.timeTable = [];
    this.ceDay = "";
    this.ceSectionId = 0;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.sections = [];
    this.ceDay = "";
    this.ceSectionId = 0;
    this.timeTable = [];
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.sections = [];
    this.ceDay = "";
    this.ceSectionId = 0;
    this.timeTable = [];
    this.ceClassId = event.target.value;
    this.getSections(this.ceSchoolId, this.ceClassId);
  }

  onSectionSelected(event) {
    this.ceSectionId = 0;
    this.timeTable = [];
    this.ceDay = "";
    this.ceSectionId = event.target.value;

    this.ceDay = this.weekday[this.d.getDay()];
    this.getTimeTable(
      this.ceSchoolId,
      this.ceClassId,
      this.ceSectionId,
      this.ceDay
    );
  }

  onSubjectSelected(event, timeTable: timeTable) {
    timeTable.subject = this.subjects.filter(
      (x) => x.id == event.target.value
    )[0];

    timeTable.staff.name = 'Click Save to update';
  }

  onDaySelected(event) {
    this.ceDay = event.target.value;
    this.getTimeTable(
      this.ceSchoolId,
      this.ceClassId,
      this.ceSectionId,
      this.ceDay
    );
  }

  showCreatePeriodPopup() {
    this.saveTimeTable(
      this.ceSchoolId,
      this.ceClassId,
      this.ceSectionId,
      this.ceDay,
      this.timeTable
    );
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

  getTimeTable(
    schoolId: number,
    classId: number,
    sectionId: number,
    day: string
  ) {
    this.timeTableService
      .getTimeTable(
        "Bearer " + this.cookie.get("token"),
        schoolId,
        classId,
        sectionId,
        day
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            if(this.role !== "STUDENT") {
              this.getSubjects();
            }
            this.timeTable = result.timeTable;
          }
        },
        (err) => {}
      );
  }

  saveTimeTable(
    schoolId: number,
    classId: number,
    sectionId: number,
    day: string,
    timeTableBody: Array<timeTable>
  ) {
    this.timeTableRequest = new timeTableRequest();
    this.timeTableRequest.timeTable = timeTableBody;
    this.timeTableService
      .saveTimeTable(
        "Bearer " + this.cookie.get("token"),
        schoolId,
        classId,
        sectionId,
        day,
        this.timeTableRequest
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getTimeTable(schoolId, classId, sectionId, day);
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
}
