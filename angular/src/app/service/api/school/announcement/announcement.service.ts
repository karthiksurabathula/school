import { Injectable } from '@angular/core';
import { GlobalVariables } from 'src/app/globalVariables';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { announcementResponse, announcement } from 'src/app/model/school/announcementResponse';

@Injectable({
  providedIn: 'root'
})
export class AnnouncementService {
  url: string = GlobalVariables.baseURL;

  constructor(private http: HttpClient) {}

  getAnnouncement(bearer: string, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.get<announcementResponse>(this.url + "api/announcement", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  createAnnouncement(bearer: string, announcement: announcement, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.post<announcementResponse>(this.url + "api/announcement", announcement, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  updateAnnouncementVisibility(bearer: string,  announcement: announcement, schoolId: number) {
    let params = new HttpParams();
    params = params.append("schoolId", schoolId.toString());

    return this.http.put<announcementResponse>(this.url + "api/announcementVisibility", announcement, {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }

  removeAnnouncement(bearer: string, schoolId: number, announcemntId: number) {
    let params = new HttpParams();
    params = params.append("announcemntId", announcemntId.toString());
    params = params.append("schoolId", schoolId.toString());

    return this.http.delete<announcementResponse>(this.url + "api/announcement", {
      headers: new HttpHeaders().set("Authorization", bearer),
      params,
    });
  }
}
