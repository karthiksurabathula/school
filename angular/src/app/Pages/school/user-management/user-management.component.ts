import { Component, OnInit, ViewChild } from "@angular/core";
import { ModalDirective, ToastService } from "ng-uikit-pro-standard";
import { city } from "src/app/model/school/cityResponse";
import { school } from "src/app/model/school/schoolResponse";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { SchoolCityService } from "src/app/service/api/school/schoolCity/schoolCity.service";
import { SchoolService } from "src/app/service/api/school/school/school.service";
import { GlobalService } from "src/app/service/global/global.service";
import { UserManagementService } from "src/app/service/api/school/userManagement/userManagement.service";
import { user } from "src/app/model/school/loginUser";

@Component({
  selector: "app-user-management",
  templateUrl: "./user-management.component.html",
  styleUrls: ["./user-management.component.css"],
})
export class UserManagementComponent implements OnInit {
  @ViewChild("basicModal", { static: true }) basicModal: ModalDirective;
  @ViewChild("resetPasswordModal", { static: true })
  resetPasswordModal: ModalDirective;

  role: string;

  cities: Array<city>;
  schools: Array<school>;
  user: Array<user>;
  currentUserEdit: user;

  ceCityId: number;
  ceSchoolId: number;

  searchText: string;

  ceAccountEnabled: boolean;
  ceAccountLocked: boolean;
  popupTitle: string;
  username: string;
  cePassword: string = "";
  cePassword1: string = "";

  constructor(
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private cityservice: SchoolCityService,
    private schoolService: SchoolService,
    private globalService: GlobalService,
    private userManageService: UserManagementService
  ) {}

  ngOnInit() {
    this.globalService.showNavBar();
    if (this.cookie.check("token") || this.cookie.get("role")) {
      this.role = this.cookie.get("role") ;
      if (this.cookie.get("role") === "SUPERUSER") {
        this.getcities();
      } else {
        this.ceSchoolId = Number(this.cookie.get("schoolId"));
        this.getUsers();
      }
    } else {
      this.router.navigate(["/login"]);
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
    this.getUsers();
  }

  // Input Validation
  checkIfPassword1Empty() {
    if (this.cePassword.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkIfPassword2Empty() {
    if (this.cePassword1.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  editUser(user: user) {
    console.log(user);
    this.currentUserEdit = user;
    this.popupTitle = "User Account Settings";
    this.username = user.username;
    this.ceAccountEnabled = user.enabled;
    this.ceAccountLocked = user.accountNonLocked;
    this.basicModal.show();
  }

  changeStatusEnabled() {
    this.currentUserEdit.enabled = !this.ceAccountEnabled;
    this.saveUserSettings();
  }

  changeStatusLocked() {
    this.currentUserEdit.accountNonLocked = !this.ceAccountLocked;
    this.saveUserSettings();
  }

  resetPasword(user: user) {
    this.cePassword = "";
    this.cePassword1 = "";
    this.username = user.username;
    this.resetPasswordModal.show();
  }

  // API
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

  getUsers() {
    this.userManageService
      .getUsers("Bearer " + this.cookie.get("token"), this.ceSchoolId)
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.user = result.users;
          } else {
            this.user = [];
          }
        },
        (err) => {}
      );
  }

  saveUserSettings() {
    this.basicModal.hide();
    this.userManageService
      .saveUserSettings(
        "Bearer " + this.cookie.get("token"),
        this.ceSchoolId,
        this.currentUserEdit
      )
      .subscribe(
        (result) => {
          if (result.indicator === "success") {
            this.getUsers();
          } else {
            this.basicModal.show();
          }
        },
        (err) => {}
      );
  }

  restUserPassword() {
    if (this.cePassword != "" || this.cePassword1 != "") {
      if (this.cePassword === this.cePassword1) {
        this.resetPasswordModal.hide();
        this.userManageService
          .resetPassword(
            "Bearer " + this.cookie.get("token"),
            this.username,
            this.cePassword,
            this.cePassword1,
            this.ceSchoolId
          )
          .subscribe(
            (result) => {
              if (result.indicator === "success") {
                this.resetPasswordModal.hide();
              } else {
                this.resetPasswordModal.show();
              }
            },
            (err) => {}
          );
      } else {
        this.toast.error("Password did not match");
      }
    } else {
      this.toast.error("Please enter Password");
    }
  }
}
