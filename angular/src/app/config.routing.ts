import { SampleDataComponent } from "./Pages/school/sample-data/sample-data.component";
import { SyllabusUpdatesComponent } from "./Pages/school/syllabus-updates/syllabus-updates.component";
import { AttendanceStudentComponent } from "./Pages/school/attendance-student/attendance-student.component";
import { PeriodTimetableStaffComponent } from "./Pages/school/period-timetable-staff/period-timetable-staff.component";
import { AttendanceTrackerComponent } from "./Pages/school/attendance-tracker/attendance-tracker.component";
import { AttendanceComponent } from "./Pages/school/attendance/attendance.component";
import { UnderMaintainanceComponent } from "./Pages/school/under-maintainance/under-maintainance.component";
import { UserManagementComponent } from "./Pages/school/user-management/user-management.component";
import { MarksStudentComponent } from "./Pages/school/marks-student/marks-student.component";
import { ExamComponent } from "./Pages/school/exam/exam.component";
import { ResetPasswordComponent } from "./Pages/school/reset-password/reset-password.component";
import { PeriodTimetableComponent } from "./Pages/school/period-timetable/period-timetable.component";
import { PeriodComponent } from "./Pages/school/period/period.component";
import { AddStaffComponent } from "./Pages/school/add-staff/add-staff.component";
import { AddSubjectToClassComponent } from "./Pages/school/add-subject-to-class/add-subject-to-class.component";
import { AddStudentComponent } from "./Pages/school/add-student/add-student.component";
import { SetupSchoolComponent } from "./Pages/school/setup-school/setup-school.component";
import { SetupCityComponent } from "./Pages/school/setup-city/setup-city.component";
import { LoginComponent } from "./Pages/school/login/Login.component";
import { RegisterComponent } from "./Pages/school/register/register.component";
import { NavbarComponent } from "./component/navbar/navbar.component";
import { Routes, RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { SetupClassComponent } from "./Pages/school/setup-class/setup-class.component";
import { SetupSectionComponent } from "./Pages/school/setup-section/setup-section.component";
import { SetupSubjectComponent } from "./Pages/school/setup-subject/setup-subject.component";
import { ExamTimetableComponent } from "./Pages/school/exam-timetable/exam-timetable.component";
import { MarksSubjectComponent } from "./Pages/school/marks-subject/marks-subject.component";
import { HomeComponent } from "./Pages/school/home/home.component";
import { AnnouncementsComponent } from "./Pages/school/announcements/announcements.component";

const routes: Routes = [
  { path: "", component: LoginComponent, pathMatch: "full" },
  { path: "announcement", component: AnnouncementsComponent },
  { path: "attendance", component: AttendanceComponent },
  { path: "attendance-student", component: AttendanceStudentComponent },
  { path: "attendance-tracker", component: AttendanceTrackerComponent },
  { path: "city", component: SetupCityComponent },
  { path: "class", component: SetupClassComponent },
  { path: "exam", component: ExamComponent },
  { path: "exam-timetable", component: ExamTimetableComponent },
  { path: "home", component: HomeComponent },
  { path: "login", component: LoginComponent },
  { path: "maintenance", component: UnderMaintainanceComponent },
  { path: "marks-student", component: MarksStudentComponent },
  { path: "marks-subject", component: MarksSubjectComponent },
  { path: "period", component: PeriodComponent },
  { path: "period-staff", component: PeriodTimetableStaffComponent },
  { path: "register", component: RegisterComponent },
  { path: "restPassword", component: ResetPasswordComponent },
  { path: "restPassword/:id", component: ResetPasswordComponent },
  { path: "sample-data", component: SampleDataComponent },
  { path: "school", component: SetupSchoolComponent },
  { path: "section", component: SetupSectionComponent },
  { path: "staff", component: AddStaffComponent },
  { path: "student", component: AddStudentComponent },
  { path: "subject", component: SetupSubjectComponent },
  { path: "subjectClass", component: AddSubjectToClassComponent },
  { path: "syllabus", component: SyllabusUpdatesComponent },
  { path: "timetable", component: PeriodTimetableComponent },
  { path: "user-management", component: UserManagementComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class ConfigRoutes {}
