import { attendanceResponse1, workingDayResponse1 } from 'src/app/model/school/attendanceResponse';
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import {
  attendanceResponse,
  attendance,
  attendanceResp,
} from "src/app/model/school/attendanceResponse";

@Injectable({
  providedIn: "root",
})
export class AttendanceService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  public getAttendance(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.get<attendanceResponse>(this.url + "api/attendance", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  public getAbsentees(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<attendanceResponse>(
      this.url + "api/absenteesBySchool",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public saveAttendance(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    attendance: Array<attendance>
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.post<attendanceResponse>(
      this.url + "api/attendance",
      attendance,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public saveAttendanceNote(
    bearer: string,
    schoolId: number,
    attendance: attendanceResp
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<attendanceResponse>(
      this.url + "api/attendanceNote",
      attendance,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public saveWorkingDay(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<workingDayResponse1>(
      this.url + "api/workingDay",
      {},
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public workingDayCheck(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<workingDayResponse1>(
      this.url + "api/workingDay",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public getAttendanceStatus(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<attendanceResponse>(
      this.url + "api/attendanceStatus",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public absenteesByStudent(bearer: string, schoolId: number, student: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("studentId", student.toString());

    return this.http.get<attendanceResponse>(
      this.url + "api/absenteesByStudent",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public absenteesByStudentId(bearer: string, schoolId: number, student: string) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("studentId", student.toString());

    return this.http.get<attendanceResponse>(
      this.url + "api/absenteesByStudentId",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  public absenteesByStudentIdNum(bearer: string, schoolId: number, student: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("studentId", student.toString());

    return this.http.get<attendanceResponse>(
      this.url + "api/absenteesByStudent",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
}
