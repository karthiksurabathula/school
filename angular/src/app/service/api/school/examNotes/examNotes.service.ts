import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { examNotes, examNotesResponse1 } from "src/app/model/school/examNotesResponse";

@Injectable({
  providedIn: "root",
})
export class ExamNotesService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  saveExamNotes(
    bearer: string,
    schoolId: number,
    classId: number,
    examId: number,
    examNotesBody: examNotes
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());
    params = params.append("classId", classId.toString());

    return this.http.post<examNotesResponse1>(
      this.url + "api/examNotes",
      examNotesBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
  updateExamNotes(
    bearer: string,
    schoolId: number,
    classId: number,
    examId: number,
    examNotesBody: examNotes
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());
    params = params.append("classId", classId.toString());

    return this.http.put<examNotesResponse1>(
      this.url + "api/examNotes",
      examNotesBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  getExamNotes(
    bearer: string,
    schoolId: number,
    classId: number,
    examId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("examId", examId.toString());
    params = params.append("classId", classId.toString());

    return this.http.get<examNotesResponse1>(this.url + "api/examNotes", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
