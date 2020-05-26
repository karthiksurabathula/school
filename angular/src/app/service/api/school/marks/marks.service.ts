import { marksResponse1 } from "src/app/model/school/marksResponse";
import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { marksResponse, marksResp } from "src/app/model/school/marksResponse";
import { marksStatusResponse } from 'src/app/model/school/marksStatus';
import { marksTrackerResponse } from 'src/app/model/school/marksTrackerResponse';

@Injectable({
  providedIn: "root",
})
export class MarksService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  saveMarksBySubject(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    examId: number,
    subjectId: number,
    marks: Array<marksResp>
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());
    params = params.append("examId", examId.toString());
    params = params.append("subjectId", subjectId.toString());

    return this.http.post<marksResponse1>(
      this.url + "api/marksBySubject",
      marks,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  getMarksBySubject(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    examId: number,
    subjectId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());
    params = params.append("examId", examId.toString());
    params = params.append("subjectId", subjectId.toString());

    return this.http.get<marksResponse>(this.url + "api/marksBySubject", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getMarksByStudent(
    bearer: string,
    schoolId: number,
    examId: number,
    studentId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());
    params = params.append("studentId", studentId.toString());

    return this.http.get<marksResponse>(this.url + "api/marksByStudent", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getMarksStatus(bearer: string, schoolId: number, examId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());

    return this.http.get<marksStatusResponse>(this.url + "api/marksStatus", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getMarksByClass(
    bearer: string,
    schoolId: number,
    classId: number,
    examId: number,
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("examId", examId.toString());

    return this.http.get<marksTrackerResponse>(this.url + "api/marksByClass", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
