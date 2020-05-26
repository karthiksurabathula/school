export class subjectClassMap {
  schoolId: number;
  classId: number;
  subjectId: number;
  optional: boolean;
  subjectName: string;
  subjectMapId: number;
}

export class subjectClassMapResponse {
  indicator: string;
  message: string;
  subjectClassMap: Array<subjectClassMap>;
}

export class subjectClassMapResponse1 {
  indicator: string;
  message: string;
  subjectClassMap: subjectClassMap;
}

export interface subjectClassMapResponse {
  subjectClassMapResp: subjectClassMapResponse;
}
