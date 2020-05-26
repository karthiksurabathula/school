export class announcement {
  id: number;
  title: string;
  description: string;
  visibilty: boolean;
  lastModified: string;
}

export class announcementResponse {
  indicator: string;
  message: string;
  announcement: Array<announcement>;
}

export class announcementResponse1 {
  indicator: string;
  message: string;
  announcement: announcement;
}

export interface announcementResponse {
    announcementResp: announcementResponse;
}
