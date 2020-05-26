import { subject } from "src/app/model/school/subjectResponse";
import { exam } from "./examResponse";

export class examTimetable {
  id: number;
  date: string;
  marks: number;
}

export class schedule {
  timetable: examTimetable;
  subject: subject;
}

export class examtimeTableResponse {
  indicator: string;
  message: string;
  exam: exam;
  schedule: Array<schedule>;
}

export class examtimeTableRequest {
  schedule: Array<schedule>;
}

export class examtimeTableResponse1 {
  indicator: string;
  message: string;
  exam: exam;
  schedule: schedule;
}

export interface examtimeTableResponse {
  examtimeTableResponse: examtimeTableResponse;
}
