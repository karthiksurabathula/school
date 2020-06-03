import { Component, OnInit } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { GlobalService } from "src/app/service/global/global.service";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { SubjectService } from "src/app/service/api/school/subject/subject.service";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { section } from "src/app/model/school/sectionResponse";
import { subjectClassMap } from "src/app/model/school/subjectClassMap";
import { SyllabusService } from "src/app/service/api/school/syllabus/syllabus.service";
import { syllabus } from "src/app/model/school/syllabusResponse";

@Component({
  selector: "app-syllabus-updates",
  templateUrl: "./syllabus-updates.component.html",
  styleUrls: ["./syllabus-updates.component.css"],
})
export class SyllabusUpdatesComponent implements OnInit {
  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  sections: Array<section>;
  subjectClassMap: Array<subjectClassMap>;
  syllabus: syllabus;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;
  ceSubjectId: any;
  role: string;
  percentage: number;
  description: string;

  constructor(
    private cookie: CookieService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private globalService: GlobalService,
    private sectionService: SectionService,
    private subjectService: SubjectService,
    private syllabusService: SyllabusService
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
      } else if (this.cookie.get("role") === "STUDENT") {
        this.ceSchoolId = +this.cookie.get("schoolId");
        this.ceClassId = +this.cookie.get("classId");
        this.ceSectionId =  +this.cookie.get("sectionId");
        this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
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
    this.description = null;
    this.percentage = null;
    this.schools = [];
    this.classes = [];
    this.sections = [];
    this.subjectClassMap = [];
    this.ceSubjectId = 0;
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.description = null;
    this.percentage = null;
    this.classes = [];
    this.sections = [];
    this.subjectClassMap = [];
    this.ceSubjectId = 0;
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.description = null;
    this.percentage = null;
    this.sections = [];
    this.subjectClassMap = [];
    this.ceSubjectId = 0;
    this.ceClassId = event.target.value;
    this.getSections(this.ceSchoolId, this.ceClassId);
  }

  onSectionSelected(event) {
    this.description = null;
    this.percentage = null;
    this.subjectClassMap = [];
    this.ceSubjectId = 0;
    this.ceSectionId = event.target.value;
    this.getSubjectByClass(this.ceSchoolId, this.ceClassId);
  }

  onSubjectId(event) {
    this.description = null;
    this.percentage = null;
    this.ceSubjectId = event.target.value;
    console.log(this.ceSubjectId);
    this.getSyllabus();
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

  getSyllabus() {
    this.syllabusService
      .getSyllabus(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceSectionId,
        this.ceSubjectId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.syllabus = result.syllabus;
            this.description = result.syllabus.description;
            this.percentage = result.syllabus.percentage;
            if (this.percentage == 0) {
              this.percentage = null;
            }
            console.log(this.syllabus);
          }
        },
        (err) => {}
      );
  }

  updateSyllabus() {
    this.syllabus.percentage = this.percentage;
    this.syllabus.description = this.description;

    this.syllabusService
      .createSyllabus(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.ceClassId,
        this.ceSectionId,
        this.ceSectionId,
        this.syllabus
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getSyllabus();
          }
        },
        (err) => {}
      );
  }
}
