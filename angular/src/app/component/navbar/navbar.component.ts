import { school, schoolResponse1 } from 'src/app/model/school/schoolResponse';
import { SchoolService } from 'src/app/service/api/school/school/school.service';
import { LoginService } from 'src/app/service/api/school/login/login.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { ModalDirective } from 'ng-uikit-pro-standard';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  @ViewChild('basicModal', { static: true }) basicModal: ModalDirective;
  @ViewChild('sidenav', { static: true }) public sidenav: any;

  constructor(
    private router: Router,
    private cookie: CookieService,
    private loginService: LoginService,
    private schoolService: SchoolService
  ) {}

  role: string;
  school: schoolResponse1;
  schoolName: string;
  address: string;
  schoolEmail: string;
  schoolPhone: string;

  ngOnInit() {
    if (this.cookie.check('token') || this.cookie.get('role')) {
      this.role = this.cookie.get('role');
    } else {
      this.router.navigate(['/login']);
    }

    this.router.events.subscribe((val) => {
      if (val instanceof NavigationStart) {
        this.sidenav.hide();
      }
    });
  }

  showContact() {
    this.getSchoolInfo();
    this.basicModal.show();
  }

  logout() {
    this.loginService.logout('Bearer ' + this.cookie.get('token')).subscribe(
      (result) => {
        if (result.indicator === 'success') {
        }
      },
      () => {}
    );
  }

  getSchoolInfo() {
    this.schoolService
      .getSchoolById(
        'Bearer ' + this.cookie.get('token'),
        Number(this.cookie.get('schoolId'))
      )
      .subscribe(
        (result) => {
          if (result.indicator === 'success') {
            this.schoolName = result.school.schoolName;
            this.address = result.school.address;
            this.schoolEmail = result.school.schoolEmail;
            this.schoolPhone = result.school.schoolPhone;
          }
        },
        () => {}
      );
  }
}
