import { staff } from './staffResponse';
import { section } from './sectionResponse';
import { subject } from "./subjectResponse";
import { period } from "./periodResponse";
import { classs } from "./classResponse";

export class timeTable {
  subject: subject;
  period: period;
  class: classs;
  section: section
  staff: staff
}

export class timeTableResponse {
  indicator: string;
  message: string;
  timeTable: Array<timeTable>;
}

export class timeTableRequest {
  timeTable: Array<timeTable>;
}

export class timeTableResponse1 {
  indicator: string;
  message: string;
  timeTable: timeTable;
}

export interface timeTableResponse {
  timeTableResponse: timeTableResponse;
}
