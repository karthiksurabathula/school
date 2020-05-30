import { exam, examResponse, examResponse1 } from "src/app/model/school/examResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class ExamService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  saveExam(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    examBody: exam
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.post<examResponse1>(this.url + "api/exam", examBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  updateExam(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    examBody: exam
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.put<examResponse1>(this.url + "api/exam", examBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getExamsBySchool(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<examResponse>(this.url + "api/exam", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getExamsBySchoolAndClass(bearer: string, schoolId: number, classId: number, sectionId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.get<examResponse>(this.url + "api/examByClass", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getExamsBySchoolScope(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<examResponse>(this.url + "api/examBySchool", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  removeExam(bearer: string, schoolId: number, examId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());

    return this.http.delete<examResponse1>(this.url + "api/exam", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getExamsByTeacher(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<examResponse>(this.url + "api/examByTeacher", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
