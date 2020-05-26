import { Injectable } from "@angular/core";
import { GlobalVariables } from 'src/app/globalVariables';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { syllabusResponse1, syllabus } from 'src/app/model/school/syllabusResponse';

@Injectable({
  providedIn: "root",
})
export class SyllabusService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getSyllabus(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    subjectId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());
    params = params.append("subjectId", subjectId.toString());

    return this.http.get<syllabusResponse1>(this.url + "api/syllabus", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  createSyllabus(
    bearer: string,
    schoolId: number,
    classId: number,
    sectionId: number,
    subjectId: number,
    syllabus: syllabus
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("sectionId", sectionId.toString());
    params = params.append("subjectId", subjectId.toString());

    return this.http.post<syllabusResponse1>(
      this.url + "api/syllabus",
      syllabus,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }
}
