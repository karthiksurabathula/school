export class subject {
  id: number;
  subjectName: string;
}

export class subjectResponse {
  indicator: string;
  message: string;
  subject: Array<subject>;
}

export class subjectResponse1 {
  indicator: string;
  message: string;
  subject: subject;
}

export interface subjectResponse {
  cityResp: subjectResponse;
}
