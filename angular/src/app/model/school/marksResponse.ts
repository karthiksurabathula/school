import { examTimetable } from "./examTimetable";
import { subject } from "./subjectResponse";
import { student } from "./studentResponse";

export class marks {
  id: number;
  marks: number;
  totalMarks: number;
}
export class marksResp {
  student: student;
  subject: subject;
  marks: marks;
}

export class marksResponse {
  indicator: string;
  message: string;
  timetable: examTimetable;
  data: Array<marksResp>;
}

export class marksResponse1 {
  indicator: string;
  message: string;
  timetable: examTimetable;
  data: marksResp;
}

export class examtimeTableRequest {
  marksReq: Array<marksResp>;
}

export interface marksResponse {
  marksResponse: marksResponse;
}
