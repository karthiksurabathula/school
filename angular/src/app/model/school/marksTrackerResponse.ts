import { student } from "./studentResponse";
import { NumberValueAccessor } from '@angular/forms';

export class marks {
  id: number;
  marksTotal: number;
  marks: number;
}

export class marksTracker {
  rank: number;
  marks: marks;
  student: student;
}

export class marksTrackerResponse {
  indicator: string;
  message: string;
  data: Array<marksTracker>;
}

export class marksTrackerResponse1 {
  indicator: string;
  message: string;
  data: marksTracker;
}

export interface marksTrackerResponse {
  marksTrackerResponse: marksTrackerResponse;
}