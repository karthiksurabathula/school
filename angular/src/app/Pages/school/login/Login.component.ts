import { Router } from "@angular/router";
import { LoginResponse } from "src/app/model/school/loginResponse";
import { LoginService } from "src/app/service/api/school/login/login.service";
import { Component, OnInit } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { ToastService } from "ng-uikit-pro-standard";
import { GlobalService } from "src/app/service/global/global.service";

@Component({
  selector: "app-Login",
  templateUrl: "./Login.component.html",
  styleUrls: ["./Login.component.css"],
})
export class LoginComponent implements OnInit {
  username = "";
  password = "";
  rememberme = false;
  user: LoginResponse;

  constructor(
    private loginService: LoginService,
    private cookie: CookieService,
    private toast: ToastService,
    private router: Router,
    private globalService: GlobalService
  ) {}

  ngOnInit() {
    this.globalService.hideNavBar();
    // this.globalService.showNavBar();
    this.cookie.delete("token");
    this.cookie.delete("role");
    this.cookie.delete("schoolId");
    this.cookie.delete("classId");
    this.cookie.delete("sectionId");
    this.cookie.delete("studentId");
    if (this.cookie.get("username") != null) {
      this.username = this.cookie.get("username");
    }
  }

  checkid() {
    if (this.username.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkpass() {
    if (this.password.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  login() {
    if (this.rememberme) {
      this.cookie.set("username", this.username, 30, "\\");
    } else {
      this.cookie.delete("username");
    }

    this.loginService.userLogin(this.username, this.password).subscribe(
      (result) => {
        if (result.token) {
          this.cookie.set("role", result.role);
          this.cookie.set("token", result.token);

          if (this.cookie.get("role") !== "SUPERUSER") {
            this.cookie.set("schoolId", result.schoolId);
            if (this.cookie.get("role") === "STUDENT") {
              this.cookie.set("classId", result.classId);
              this.cookie.set("sectionId", result.sectionId);
              this.cookie.set("studentId", result.studentId);
            }
          }
        }
      },
      (err) => {
        this.toast.error("Login Failed");
      }
    );
  }

  register() {
    this.router.navigate(["/register"]);
  }

  httpSuccessResponseHandler(result: any) {
    this.toast.error(result.message);
  }

  handleError(result: any) {
    this.toast.error(result.message);
  }
}
