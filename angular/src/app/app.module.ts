import { SampleDataComponent } from './Pages/school/sample-data/sample-data.component';
import { AnnouncementsComponent } from './Pages/school/announcements/announcements.component';
import { SyllabusUpdatesComponent } from './Pages/school/syllabus-updates/syllabus-updates.component';
import { AttendanceStudentComponent } from './Pages/school/attendance-student/attendance-student.component';
import { PeriodTimetableStaffComponent } from './Pages/school/period-timetable-staff/period-timetable-staff.component';
import { AttendanceTrackerComponent } from './Pages/school/attendance-tracker/attendance-tracker.component';
import { AttendanceComponent } from './Pages/school/attendance/attendance.component';
import { HomeComponent } from './Pages/school/home/home.component';
import { UnderMaintainanceComponent } from './Pages/school/under-maintainance/under-maintainance.component';
import { UserManagementComponent } from './Pages/school/user-management/user-management.component';
import { MarksSubjectComponent } from "./Pages/school/marks-subject/marks-subject.component";
import { ExamComponent } from "./Pages/school/exam/exam.component";
import { ResetPasswordComponent } from "./Pages/school/reset-password/reset-password.component";
import { PeriodTimetableComponent } from "./Pages/school/period-timetable/period-timetable.component";
import { PeriodComponent } from "./Pages/school/period/period.component";
import { AddStaffComponent } from "./Pages/school/add-staff/add-staff.component";
import { AddSubjectToClassComponent } from "./Pages/school/add-subject-to-class/add-subject-to-class.component";
import { SetupSubjectComponent } from "./Pages/school/setup-subject/setup-subject.component";
import { AddStudentComponent } from "./Pages/school/add-student/add-student.component";
import { HttpInterceptorService } from "./service/http-interceptor/http-interceptor.service";
import { SetupClassComponent } from "./Pages/school/setup-class/setup-class.component";
import { SetupSchoolComponent } from "./Pages/school/setup-school/setup-school.component";
import { SetupCityComponent } from "./Pages/school/setup-city/setup-city.component";
import { RegisterComponent } from "./Pages/school/register/register.component";
import { LoginComponent } from "./Pages/school/login/Login.component";
import { ConfigRoutes } from "./config.routing";
import { NavbarComponent } from "./component/navbar/navbar.component";

import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule, NO_ERRORS_SCHEMA } from "@angular/core";
import { FormsModule } from "@angular/forms";
import {
  HttpClientModule,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
} from "@angular/common/http";
import { AgmCoreModule } from "@agm/core";
import { AppComponent } from "./app.component";
import { CookieService } from "ngx-cookie-service";

import {
  MDBSpinningPreloader,
  MDBBootstrapModulesPro,
  ToastModule,
} from "ng-uikit-pro-standard";
import { Ng2SearchPipeModule } from "ng2-search-filter";
import { SetupSectionComponent } from "./Pages/school/setup-section/setup-section.component";
import { ExamTimetableComponent } from "./Pages/school/exam-timetable/exam-timetable.component";
import { MarksStudentComponent } from "./Pages/school/marks-student/marks-student.component";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    ResetPasswordComponent,
    SetupCityComponent,
    SetupSchoolComponent,
    SetupClassComponent,
    SetupSectionComponent,
    AddStudentComponent,
    SetupSubjectComponent,
    AddSubjectToClassComponent,
    AddStaffComponent,
    PeriodComponent,
    PeriodTimetableComponent,
    PeriodTimetableStaffComponent,
    ExamComponent,
    ExamTimetableComponent,
    MarksSubjectComponent,
    MarksStudentComponent,
    UserManagementComponent,
    UnderMaintainanceComponent,
    HomeComponent,
    AttendanceComponent,
    AttendanceTrackerComponent,
    AttendanceStudentComponent,
    SyllabusUpdatesComponent,
    AnnouncementsComponent,
    SampleDataComponent
  ],
  imports: [
    HttpClientModule,
    Ng2SearchPipeModule,
    ConfigRoutes,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ToastModule.forRoot(),
    MDBBootstrapModulesPro.forRoot(),
    AgmCoreModule.forRoot({
      // https://developers.google.com/maps/documentation/javascript/get-api-key?hl=en#key
      apiKey: "Your_api_key",
    }),
  ],
  providers: [
    MDBSpinningPreloader,
    CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
  schemas: [NO_ERRORS_SCHEMA],
})
export class AppModule {}
