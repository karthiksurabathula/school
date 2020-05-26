import { Component, OnInit } from "@angular/core";
import { LoginService } from "src/app/service/api/school/login/login.service";
import { Router, ActivatedRoute } from "@angular/router";
import { ToastService } from "ng-uikit-pro-standard";
import { GlobalService } from "src/app/service/global/global.service";
import { CookieService } from "ngx-cookie-service";

@Component({
  selector: "app-reset-password",
  templateUrl: "./reset-password.component.html",
  styleUrls: ["./reset-password.component.css"],
})
export class ResetPasswordComponent implements OnInit {
  hideCurrenPassword: boolean;
  password1 = "";
  password = "";
  passResetId = "";
  currentPassword = "";

  constructor(
    private loginServie: LoginService,
    private router: Router,
    private toast: ToastService,
    private globalService: GlobalService,
    private route: ActivatedRoute,
    private cookie: CookieService
  ) {}

  ngOnInit() {
    // this.cookie.delete('token');
    // this.cookie.delete('role');

    if (this.cookie.check("token")) {
      this.hideCurrenPassword = true;
      this.globalService.showNavBar();
    } else {
      this.hideCurrenPassword = false;
      this.globalService.hideNavBar();
    }

    this.route.paramMap.subscribe((params) => {
      this.passResetId = params.get("id");
    });
  }

  checkCurPass() {
    if (this.currentPassword.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkPass() {
    if (this.password.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  checkPass1() {
    if (this.password1.length > 0) {
      return false;
    } else {
      return true;
    }
  }

  register() {
    if (this.hideCurrenPassword) {
      if (
        this.password != "" ||
        this.password1 != "" ||
        this.currentPassword != ""
      ) {
        if (this.password === this.password1) {
          this.loginServie
            .resetPassWithToken(
              "Bearer " + this.cookie.get("token"),
              this.currentPassword,
              this.password,
              this.password1,
              this.passResetId
            )
            .subscribe(
              (result) => {},
              (err) => {}
            );
        } else {
          this.toast.error("New Password did not match");
        }
      } else {
        this.toast.error("Please fill missing fields");
      }
    } else {
      if (this.password != "" || this.password1 != "") {
        if (this.password === this.password1) {
          this.loginServie
            .reset(this.password, this.password1, this.passResetId)
            .subscribe(
              (result) => {},
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
}
