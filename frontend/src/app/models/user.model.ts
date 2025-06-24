export interface User {
  id: string;
  username: string;
  name: string;
  email: string;
  profile: string[];
  minCH: number | null;
  status: 'Ativo' | 'Suspenso';
} 