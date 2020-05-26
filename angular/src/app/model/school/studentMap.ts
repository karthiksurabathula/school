export class studentMap {
  studentId: number;
  schoolId: number;
  classId: number;
  sectionId: number;
}

export class studentMapResponse {
  indicator: string;
  message: string;
  studentMap: Array<studentMap>;
}

export class studentMapResponse1 {
  indicator: string;
  message: string;
  studentMap: studentMap;
}

export interface studentMapResponse {
  studentMapResp: studentMapResponse;
}
