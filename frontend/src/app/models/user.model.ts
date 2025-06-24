export interface User {
  id: string;
  username: string;
  name: string;
  email: string;
  phone: string;
  workload: number | null;
  status: 'ACTIVE' | 'SUSPENDED';
  profiles: Profile[];
}

export interface Profile {
  id: number;
  role: ProfileRole;
  description: string;
  protectedProfile: boolean;
}

export type ProfileRole = 
  | 'ADMINISTRATOR'
  | 'COURSE_COORDINATOR'
  | 'ASSISTANT_COURSE_COORDINATOR'
  | 'PROFESSOR'
  | 'ACADEMIC_SECRETARY'
  | 'TECHNICAL_PEDAGOGICAL_STAFF'
  | 'STUDENT'
  | 'PEDAGOGICAL_CURATOR'
  | 'DIRECTOR'
  | 'DEPUTY_DIRECTOR'
  | 'BOARD_MEMBER'; 