import { SchoolService } from "src/app/service/api/school/school/school.service";
import { school } from "src/app/model/school/schoolResponse";
import { Component, OnInit, HostListener, ViewChild } from "@angular/core";
import {
  ModalDirective, ToastService
} from "ng-uikit-pro-standard";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { city } from "src/app/model/school/cityResponse";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { GlobalService } from "src/app/service/global/global.service";

@Component({
  selector: "app-setup-school",
  templateUrl: "./setup-school.component.html",
  styleUrls: ["./setup-school.component.css"],
})
export class SetupSchoolComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("conformationModal", { static: true }) conformationModal: ModalDirective;

  hiddenByRole: boolean;
  searchText: string;

  editSchool: boolean = false;
  popupTitle: string;
  updateFlag: boolean;
  role: string;

  schools: Array<school>;

  cities: Array<city>;

  //formvariables
  ceCityId: number;
  ceSchoolId: number;
  ceSchoolCode: string = "";
  ceSchoolName: string = "";
  ceSchoolPhone: string = "";
  ceSchoolEmail: string = "";
  ceSchoolLocation: string = "";
  ceSchoolAddress: string = "";
  ceStatus: boolean;

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    this.ceCityId = 0;
    if (this.cookie.check("token") || this.cookie.get("role")) {
      this.role = this.cookie.get("role");
      if (this.cookie.get("role") === "SUPERUSER") {
        this.hiddenByRole = true;
        this.getcities();
      } else {
        this.hiddenByRole = false;
        this.getSchoolsByUser();
      }
    } else {
      this.router.navigate(["/login"]);
    }
  }

  //Popup
  showCreateSchoolPopup() {
    this.popupTitle = "Create School";
    this.ceSchoolId = 0;
    this.ceSchoolCode = "";
    this.ceSchoolName = "";
    this.ceSchoolPhone = "";
    this.ceSchoolEmail = "";
    this.ceSchoolLocation = "";
    this.ceSchoolAddress = "";
    this.ceStatus = true;
    this.updateFlag = false;
    this.basicModal.show();
  }

  //Dropdown Select Event
  onOptionsSelected(event) {
    this.ceCityId = event.target.value;
    this.getSchools(this.ceCityId);
  }

  changeStatus() {
    this.ceStatus = !this.ceStatus;
  }

  //Buttons
  editSchoolPopup(school: school) {
    this.popupTitle = "Edit School";
    this.ceSchoolId = school.id;
    this.ceSchoolCode = school.schoolCode;
    this.ceSchoolName = school.schoolName;
    this.ceSchoolPhone = school.schoolPhone;
    this.ceSchoolEmail = school.schoolEmail;
    this.ceSchoolLocation = school.location;
    this.ceSchoolAddress = school.address;
    this.ceStatus = school.status;
    this.updateFlag = true;
    this.basicModal.show();
  }

  removeConformationPopup(school: any) {
    this.ceSchoolId = school.id;
    this.conformationModal.show();
  }

  //Input Validation
  checkIfCodeIsEmpty() {
    if (this.ceSchoolCode.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfNameIsEmpty() {
    if (this.ceSchoolName.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfPhoneIsEmpty() {
    if (this.ceSchoolPhone.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfEmailIsEmpty() {
    if (this.ceSchoolEmail.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfLocationIsEmpty() {
    if (this.ceSchoolLocation.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfAddressIsEmpty() {
    if (this.ceSchoolAddress.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  getCitiesByRole() {
    if (this.cookie.get("role") === "SUPERUSER") {
      this.getSchools(this.ceCityId);
    } else {
      this.getSchoolsByUser();
    }
  }

  //API
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

  getSchoolsByUser() {
    this.schoolService
      .getSchoolsByUser("Bearer " + this.cookie.get("token"))
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.schools = result.school;
          }
        },
        (err) => {}
      );
  }

  add() {
    if (
      !this.checkIfCodeIsEmpty() &&
      !this.checkIfNameIsEmpty() &&
      !this.checkIfPhoneIsEmpty() &&
      !this.checkIfEmailIsEmpty() &&
      !this.checkIfLocationIsEmpty() &&
      !this.checkIfAddressIsEmpty()
    ) {
      if (this.updateFlag) {
        this.basicModal.hide();
        this.schoolService
          .updateSchool("Bearer " + this.cookie.get("token"), {
            id: this.ceSchoolId,
            schoolCode: this.ceSchoolCode,
            schoolName: this.ceSchoolName,
            schoolPhone: this.ceSchoolPhone,
            schoolEmail: this.ceSchoolEmail,
            location: this.ceSchoolLocation,
            address: this.ceSchoolAddress,
            status: this.ceStatus,
          })
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.getCitiesByRole();

                this.ceSchoolId = 0;
                this.ceSchoolCode = "";
                this.ceSchoolName = "";
                this.ceSchoolPhone = "";
                this.ceSchoolEmail = "";
                this.ceSchoolLocation = "";
                this.ceSchoolAddress = "";
                this.ceStatus = true;

                this.basicModal.hide();
              } else {
                this.basicModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.basicModal.hide();
        this.schoolService
          .cerateSchool(
            "Bearer " + this.cookie.get("token"),
            {
              id: this.ceSchoolId,
              schoolCode: this.ceSchoolCode,
              schoolName: this.ceSchoolName,
              schoolPhone: this.ceSchoolPhone,
              schoolEmail: this.ceSchoolEmail,
              location: this.ceSchoolLocation,
              address: this.ceSchoolAddress,
              status: this.ceStatus,
            },
            this.ceCityId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.getCitiesByRole();

                this.ceSchoolId = 0;
                this.ceSchoolCode = "";
                this.ceSchoolName = "";
                this.ceSchoolPhone = "";
                this.ceSchoolEmail = "";
                this.ceSchoolLocation = "";
                this.ceSchoolAddress = "";
                this.ceStatus = true;
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
    this.schoolService
      .removeSchool("Bearer " + this.cookie.get("token"), this.ceSchoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getSchools(this.ceCityId);
            this.conformationModal.hide();
          } else {
            this.conformationModal.show();
          }
        },
        (err) => {}
      );
  }
}
