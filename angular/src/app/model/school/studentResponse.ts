export class student {
  id: number;
  studentId: string;
  name: string;
  gender: string;
  dob: string;
  address: string;
  bloodGroup: string;
  fatherName: string;
  fatherPhoneNo: string;
  fatherEmail: string;
  motherName: string;
  motherPhoneNo: string;
  motherEmail: string;
  pending: boolean;
  status: boolean;
}

export class studentResponse {
  indicator: string;
  message: string;
  student: Array<student>;
}

export class studentResponse1 {
  indicator: string;
  message: string;
  student: student;
}

export interface studentResponse {
  studentResp: studentResponse;
}
