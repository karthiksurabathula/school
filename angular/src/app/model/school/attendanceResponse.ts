import { student } from './studentResponse';
import { classs } from './classResponse';
import { section } from './sectionResponse';


export class workingDay {
    id: number;
    date: string;
}

export class attendanceResp {
    id: number;
    absent: boolean;
    note: string;
}

export class attendanceTrackerResp {
  id: number;
  completed: boolean;
}

export class attendance {
    student: student;
    class: classs;
    section: section;
    day: workingDay;
    attendance: attendanceResp;
    attendanceTracker: attendanceTrackerResp;
}

export class attendanceResponse {
  indicator: string;
  message: string;
  data: Array<attendance>;
}

export class attendanceResponse1 {
  indicator: string;
  message: string;
  data: attendance;
}

export class workingDayResponse1 {
  indicator: string;
  message: string;
  day: workingDay;
}

export interface attendanceResponse {
  attendanceResp: attendanceResponse;
}
