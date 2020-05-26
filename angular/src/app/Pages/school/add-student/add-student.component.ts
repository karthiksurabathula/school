import { student } from "src/app/model/school/studentResponse";
import { StudentService } from "src/app/service/api/school/student/student.service";
import { Component, OnInit, ViewChild } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import {
  ToastService,
  ModalDirective,
  MDBDatePickerComponent,
  IMyOptions,
} from "ng-uikit-pro-standard";
import { Router } from "@angular/router";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { ClassService } from "src/app/service/api/school/class/class.service";
import { GlobalService } from "src/app/service/global/global.service";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { classs } from "src/app/model/school/classResponse";
import { SectionService } from "src/app/service/api/school/section/section.service";
import { section } from "src/app/model/school/sectionResponse";

@Component({
  selector: "app-add-student",
  templateUrl: "./add-student.component.html",
  styleUrls: ["./add-student.component.css"],
})
export class AddStudentComponent implements OnInit {
  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private classService: ClassService,
    private studentService: StudentService,
    private globalService: GlobalService,
    private sectionService: SectionService
  ) {}

  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("basicClassModal", { static: true })
  basicClassModal: ModalDirective;
  @ViewChild("datePicker", { static: true }) datePicker: MDBDatePickerComponent;
  @ViewChild("conformationModal", { static: true })
  conformationModal: ModalDirective;

  role: string;
  updateFlag = false;
  searchText: string;

  cities: Array<city>;
  schools: Array<school>;
  classes: Array<classs>;
  students: Array<student>;
  sections: Array<section>;
  showpPending: boolean;

  ceCityId: number;
  ceSchoolId: number;
  ceClassId: number;
  ceSectionId: number;
  ceStudentId: any;
  ceClassMapId: number;
  ceSectionMapId: number;
  popupTitle: string;
  ceStudentSerach = "";

  cestuId = 0;
  ceStudentName = "";
  ceDob = "";
  ceGender = "";
  ceBloodGroup: string;
  ceFatherName = "";
  ceFatherEmail = "";
  ceFatherPhone = "";
  ceMotherName = "";
  ceMotherEmail = "";
  ceMotherPhone = "";
  ceStudentAddress = "";
  cePending: boolean;
  dpGenderSelect: any;
  dpClassSelect: any;
  dpBloodGroupSelect: any;
  dpSectionSelect: any;
  cestatus: boolean;

  radioChecked: boolean;

  gender: Array<any>;
  bloodGroup: Array<any>;

  public myDatePickerOptions: IMyOptions = {
    minYear: new Date().getFullYear() - 150,
    maxYear: new Date().getFullYear(),
    closeAfterSelect: true,
    dateFormat: "d-m-yyyy",
  };

  ngOnInit() {
    this.globalService.showNavBar();
    this.students = [];
    // Date
    this.gender = [
      { value: "male", label: "Male" },
      { value: "female", label: "Female" },
    ];

    this.bloodGroup = [
      { value: "A+", label: "A+" },
      { value: "A-", label: "A-" },
      { value: "B+", label: "B+" },
      { value: "B-", label: "B-" },
      { value: "O+", label: "O+" },
      { value: "O-", label: "O-" },
      { value: "AB+", label: "AB+" },
      { value: "AB-", label: "AB-" },
    ];

    this.ceSchoolId = 0;
    this.ceClassId = 0;
    this.radioChecked = true;
    this.showpPending = false;

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

  // Button
  searchStudent() {
    this.showpPending = false;
    this.getStudentId(this.ceStudentSerach);
  }

  showPendingStudents() {
    this.showpPending = true;
    this.getPendingStudentsBySchool();
  }

  // Popup
  showCreateStudentPopup() {
    this.updateFlag = false;
    this.popupTitle = "Create Student";

    this.cestuId = 0;
    this.ceStudentName = "";
    this.ceStudentId = "";
    this.ceDob = "";
    this.ceGender = "";
    this.ceBloodGroup = "";
    this.ceFatherName = "";
    this.ceFatherEmail = "";
    this.ceFatherPhone = "";
    this.ceMotherName = "";
    this.ceMotherEmail = "";
    this.ceMotherPhone = "";
    this.ceStudentAddress = "";
    this.dpGenderSelect = "disabled";
    this.dpBloodGroupSelect = "disabled";
    this.dpClassSelect = "disabled";
    this.cestatus = true;
    this.basicModal.show();
  }

  editStudentPopup(studentObj: student) {
    this.updateFlag = true;
    this.cestuId = studentObj.id;
    this.ceStudentName = studentObj.name;
    this.ceStudentId = studentObj.studentId;
    this.ceDob = studentObj.dob;
    this.ceFatherName = studentObj.fatherName;
    this.ceFatherEmail = studentObj.fatherEmail;
    this.ceFatherPhone = studentObj.fatherPhoneNo;
    this.ceMotherName = studentObj.motherName;
    this.ceMotherEmail = studentObj.motherEmail;
    this.ceMotherPhone = studentObj.motherPhoneNo;
    this.ceStudentAddress = studentObj.address;
    this.cestatus = studentObj.status;
    this.dpGenderSelect = studentObj.gender;
    this.dpBloodGroupSelect = studentObj.bloodGroup;
    this.cePending = studentObj.pending;

    this.basicModal.show();
  }

  classMapPopup(studentObj: student) {
    this.getStudentMapByStudentId(studentObj);
  }

  remove(studentObj: student) {
    this.ceStudentId = studentObj.id;
    this.conformationModal.show();
  }

  // Check if Text box is empty

  checkIfStudentNameIsEmpty() {
    if (this.ceStudentName.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfMotherNameIsEmpty() {
    if (this.ceMotherName.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfFatherNameIsEmpty() {
    if (this.ceFatherName.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfStudentIdIsEmpty() {
    if (this.ceStudentSerach.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  // Dropdown Select Event
  onCitySelected(event) {
    this.schools = [];
    this.classes = [];
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  onSchoolSelected(event) {
    this.classes = [];
    this.ceSchoolId = event.target.value;
    this.getClasses(this.ceSchoolId);
  }

  onClassSelected(event) {
    this.ceClassId = event.target.value;
  }

  onClassMapSelected(event) {
    this.ceClassMapId = event.target.value;
    this.getSections(this.ceSchoolId, this.ceClassMapId);
  }

  onSectionMapSelected(event) {
    this.ceSectionMapId = event.target.value;
  }

  onGenderSelected(event) {
    this.ceGender = event.target.value;
  }

  onBloodGroupSelected(event) {
    this.ceBloodGroup = event.target.value;
  }

  radioChangeHandler(event: any) {
    const selected = event.target.value;
    if (selected.localeCompare("id") === 0) {
      this.radioChecked = true;
    } else {
      this.radioChecked = false;
    }
  }

  reloadStudents() {
    if (this.showpPending) {
      this.getPendingStudentsBySchool();
    } else {
      if (this.radioChecked) {
        this.getStudentId(this.ceStudentId);
      } else {
        this.getStudentsByClass();
      }
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

  add() {
    if (this.updateFlag) {
      if (
        !this.checkIfFatherNameIsEmpty() &&
        !this.checkIfMotherNameIsEmpty() &&
        !this.checkIfStudentNameIsEmpty()
      ) {
        this.basicModal.hide();
        this.studentService
          .updateStudent(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.cestuId,
              studentId: this.ceStudentId,
              name: this.ceStudentName,
              gender: this.ceGender,
              dob: this.ceDob,
              address: this.ceStudentAddress,
              bloodGroup: this.ceBloodGroup,
              fatherName: this.ceFatherName,
              fatherPhoneNo: this.ceFatherPhone,
              fatherEmail: this.ceFatherEmail,
              motherName: this.ceMotherName,
              motherPhoneNo: this.ceMotherPhone,
              motherEmail: this.ceMotherEmail,
              pending: true,
              status: this.cestatus,
            },
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                // if (this.cestuId !== 0) {
                this.reloadStudents();
                this.basicModal.hide();
                // }
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.toast.error("Please fill all the mandatory fields");
      }
    } else {
      if (
        !this.checkIfFatherNameIsEmpty() &&
        !this.checkIfMotherNameIsEmpty() &&
        !this.checkIfStudentNameIsEmpty()
      ) {
        this.basicModal.hide();
        this.studentService
          .cerateStudent(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.cestuId,
              studentId: this.ceStudentId,
              name: this.ceStudentName,
              gender: this.ceGender,
              dob: this.ceDob,
              address: this.ceStudentAddress,
              bloodGroup: this.ceBloodGroup,
              fatherName: this.ceFatherName,
              fatherPhoneNo: this.ceFatherPhone,
              fatherEmail: this.ceFatherEmail,
              motherName: this.ceMotherName,
              motherPhoneNo: this.ceMotherPhone,
              motherEmail: this.ceMotherEmail,
              pending: true,
              status: this.cestatus,
            },
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                // if (this.cestuId !== 0) {
                this.getPendingStudentsBySchool();
                this.basicModal.hide();
                // }
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.toast.error("Please fill all the mandatory fields");
      }
    }
  }

  getStudentId(studentId: string) {
    this.studentService
      .getStudentsByStudentId(
        "Bearer " + this.cookie.get("token"),
        studentId,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.students = [];
            this.students = result.student;
          } else {
            this.students = [];
          }
        },
        (err) => {}
      );
  }

  deleteStudent() {
    this.conformationModal.hide();
    this.studentService
      .deleteStudentMapByStudentId(
        "Bearer " + this.cookie.get("token"),
        this.ceStudentId,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.reloadStudents();
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
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

  getStudentsByClass() {
    this.showpPending = false;
    this.studentService
      .getStudentsByClassId(
        "Bearer " + this.cookie.get("token"),
        this.ceClassId,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.students = [];
            this.students = result.student;
          }
        },
        (err) => {}
      );
  }

  getStudentMapByStudentId(studentObj: student) {
    this.studentService
      .getStudentMapByStudentId(
        "Bearer " + this.cookie.get("token"),
        studentObj.id,
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.dpClassSelect = result.studentMap.classId;
            this.getSections(this.ceSchoolId, result.studentMap.classId);
            this.dpSectionSelect = result.studentMap.sectionId;
            this.ceClassMapId = result.studentMap.classId;
            this.ceSectionMapId = result.studentMap.sectionId;
          } else if (result.indicator === "fail") {
            if (typeof result.studentMap === "undefined") {
              this.dpClassSelect = "disabled";
              this.dpSectionSelect = "disabled";
            }
          }
          this.cestuId = studentObj.id;
          this.ceStudentName = studentObj.name;
          this.basicClassModal.show();
        },
        (err) => {}
      );
  }

  addStudentMap() {
    if (
      this.dpSectionSelect !== "disabled" &&
      this.dpSectionSelect !== "disabled"
    ) {
      this.basicClassModal.hide();
      this.studentService
        .cerateStudentMap("Bearer " + this.cookie.get("token"), {
          schoolId: this.ceSchoolId,
          classId: this.ceClassMapId,
          sectionId: this.ceSectionMapId,
          studentId: this.cestuId,
        })
        .subscribe(
          (result) => {
            if (result.indicator === "success") {
              this.basicClassModal.hide();
              this.reloadStudents();
            } else {
              this.basicClassModal.show();
            }
          },
          (err) => {}
        );
    } else {
      this.toast.error("Please Select Class & Section");
    }
  }

  getPendingStudentsBySchool() {
    this.studentService
      .getPendingStudentBySchool(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.students = [];
            this.students = result.student;
          }
        },
        (err) => {}
      );
  }
}
