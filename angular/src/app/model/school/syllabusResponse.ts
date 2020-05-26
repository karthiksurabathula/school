export class syllabus {
  id: number;
  description: string;
  percentage: number;
}

export class syllabusResponse1 {
  indicator: string;
  message: string;
  syllabus: syllabus;
}

export interface announcementResponse {
  syllabusResp: syllabusResponse1;
}
