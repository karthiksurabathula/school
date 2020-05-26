import { Injectable } from "@angular/core";
import { GlobalVariables } from "src/app/globalVariables";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import {
  subjectResponse,
  subjectResponse1,
  subject,
} from "src/app/model/school/subjectResponse";
import {
  subjectClassMap,
  subjectClassMapResponse,
  subjectClassMapResponse1,
} from "src/app/model/school/subjectClassMap";

@Injectable({
  providedIn: "root",
})
export class SubjectService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getSubjects(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<subjectResponse>(this.url + "api/subject", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  cerateSubject(bearer: string, subjBody: subject, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<subjectResponse1>(
      this.url + "api/subject",
      subjBody,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  updateSubject(bearer: string, subjBody: subject, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.put<subjectResponse1>(this.url + "api/subject", subjBody, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  removeSubject(bearer: string, classId: number, schoolId: number) {
    let params = new HttpParams();
    params = params.append("subjectId", classId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<subjectResponse1>(this.url + "api/subject", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  cerateSubjectClassMap(bearer: string, subClassMap: subjectClassMap) {
    return this.http.post<subjectResponse1>(
      this.url + "api/subjectClassMap",
      subClassMap,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
      }
    );
  }

  updateSubjectClassMap(bearer: string, subClassMap: subjectClassMap) {
    return this.http.put<subjectClassMapResponse1>(
      this.url + "api/subjectClassMap",
      subClassMap,
      {
        headers: new HttpHeaders().set("Authorization", bearer),
      }
    );
  }

  getSubjectClassMap(bearer: string, schoolId: number, classId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());

    return this.http.get<subjectClassMapResponse>(
      this.url + "api/subjectClassMap",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  removeSubjectClassMap(
    bearer: string,
    schoolId: number,
    subjectMapId: number
  ) {
    let params = new HttpParams();
    params = params.append("subjectMapId", subjectMapId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<subjectClassMapResponse>(
      this.url + "api/subjectClassMap",
      {
        headers: new HttpHeaders().set("Authorization", bearer),
        params,
      }
    );
  }

  createSubjectStaffMap(
    bearer: string,
    schoolId: number,
    classId: number,
    subjectId: number,
    staffId: number,
    sectionId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("subjectId", subjectId.toString());
    params = params.append("staffId", staffId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.post<any>(this.url + "api/subjectStaffMap", "", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  getSubjectStaffMap(
    bearer: string,
    schoolId: number,
    classId: number,
    subjectId: number,
    sectionId: number
  ) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());
    params = params.append("classId", classId.toString());
    params = params.append("subjectId", subjectId.toString());
    params = params.append("sectionId", sectionId.toString());

    return this.http.get<any>(this.url + "api/subjectStaffMap", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
