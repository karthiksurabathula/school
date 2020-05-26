import { subject } from "./subjectResponse";
import { classs } from "./classResponse";
import { section } from "./sectionResponse";

export class subjectStatus {
  id: number;
  completed: boolean;
}

export class marksStatus {
  subjectStatus: subjectStatus;
  subject: subject;
  class: classs;
  section: section;
}

export class marksStatusResponse {
  indicator: string;
  message: string;
  examMarksStatus: Array<marksStatus>;
}

export class marksStatusResponse1 {
  indicator: string;
  message: string;
  examMarksStatus: marksStatus;
}

export interface marksStatusResponse {
  marksStatusResponse: marksStatusResponse;
}
