import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpParams, HttpHeaders, HttpClient } from "@angular/common/http";
import {
  examtimeTableResponse,
  examtimeTableRequest,
  examtimeTableResponse1,
  schedule,
} from "src/app/model/school/examTimetable";

@Injectable({
  providedIn: "root",
})
export class ExamTimetableService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getExamTimeTable(
    bearer: string,
    schoolId: number,
    classId: number,
    examId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("examId", examId.toString());

    return this.http.get<examtimeTableResponse>(
      this.url + "api/examTimetable",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  getExamTimeTableBySchool(bearer: string, schoolId: number, examId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());

    return this.http.get<examtimeTableResponse>(
      this.url + "api/examTimetableSchool",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  saveExamTimeTable(
    bearer: string,
    schoolId: number,
    classId: number,
    examId: number,
    examTimetableBody: examtimeTableRequest
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("examId", examId.toString());

    return this.http.post<examtimeTableResponse1>(
      this.url + "api/examTimetable",
      examTimetableBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
}
