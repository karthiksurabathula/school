import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import {
  studentResponse,
  studentResponse1,
  student,
} from "src/app/model/school/studentResponse";
import {
  studentMapResponse1,
  studentMapResponse,
  studentMap,
} from "src/app/model/school/studentMap";

@Injectable({
  providedIn: "root",
})
export class StudentService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getStudentsByClassId(bearer: string, calssId: number, schoolId: number) {
    let params = new HttpParams();
    params = params.append("classId", calssId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<studentResponse>(this.url + "api/studentByClass", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getStudentsByStudentId(bearer: string, studentId: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("studentId", studentId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<studentResponse>(this.url + "api/student", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  cerateStudent(bearer: string, studentBody: student, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<studentResponse1>(
      this.url + "api/student",
      studentBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  updateStudent(bearer: string, studentBody: student, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.put<studentResponse1>(
      this.url + "api/student",
      studentBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  getStudentMapByStudentId(
    bearer: string,
    studentId: number,
    schoolId: number
  ) {
    let params = new HttpParams();
    params = params.append("studentId", studentId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<studentMapResponse1>(this.url + "api/student/map", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  deleteStudentMapByStudentId(
    bearer: string,
    studentId: number,
    schoolId: number
  ) {
    let params = new HttpParams();
    params = params.append("studentId", studentId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<studentMapResponse1>(this.url + "api/student", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getStudentById(
    bearer: string,
    studentId: number,
    schoolId: number
  ) {
    let params = new HttpParams();
    params = params.append("studentId", studentId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<studentResponse1>(this.url + "api/studentById", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  cerateStudentMap(bearer: string, studentMapBody: studentMap) {
    return this.http.post<studentMapResponse>(
      this.url + "api/student/map",
      studentMapBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
      }
    );
  }

  getPendingStudentBySchool(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<studentResponse>(this.url + "api/student/pending", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
