import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User, Profile } from '../models/user.model';

export interface PagedResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

// Interface para atualização de usuário conforme esperado pelo backend
export interface UserUpdateRequest {
  username?: string;
  name?: string;
  email?: string;
  phone?: string;
  workload?: number | null;
  status?: string;
  profileIds?: number[];
}

@Injectable({ providedIn: 'root' })
export class UserService {
  // URL base da API - ajuste conforme necessário
  private readonly apiUrl = 'http://localhost:8080/users';

  // Flag para controlar se deve usar dados mockados
  private useMockData = false;

  constructor(private http: HttpClient) {}

  getUsers(page = 0, size = 10, sortBy = 'name', sortDir = 'asc'): Observable<PagedResult<User>> {
    if (this.useMockData) {
      return this.getMockUsers(page, size);
    }

    // Headers para evitar cache e configurar CORS
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Cache-Control': 'no-cache, no-store, must-revalidate',
      'Pragma': 'no-cache',
      'Expires': '0'
    });

    let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
        .set('sortBy', sortBy)
        .set('sortDir', sortDir);

    return this.http.get<PagedResult<User>>(this.apiUrl, { params, headers })
        .pipe(
            tap(response => {
              console.log('Response from backend:', response);
              console.log('Number of users:', response.content?.length || 0);
            }),
            catchError(this.handleError)
        );
  }

  findUsersByUserName(
  username: string,
  page: number = 0,
  
): Observable<PagedResult<User>> {
  const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'Cache-Control': 'no-cache, no-store, must-revalidate',
    'Pragma': 'no-cache',
    'Expires': '0'
  });

  // Monta os params, incluindo o filtro de nome
  let params = new HttpParams()
    .set('username', username)                       // adiciona o filtro de busca
    


  return this.http.get<PagedResult<User>>( `${this.apiUrl}/search`, { params, headers })
    .pipe(
      tap(response => {
        console.log('Response from backend:', response);
        console.log('Number of users:', response.content?.length || 0);
      }),
      catchError(this.handleError)
    );
}


  getUser(id: string): Observable<User> {
    if (this.useMockData) {
      return this.getMockUser(id);
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Cache-Control': 'no-cache, no-store, must-revalidate',
      'Pragma': 'no-cache',
      'Expires': '0'
    });

    return this.http.get<User>(`${this.apiUrl}/${id}`, { headers })
        .pipe(
            catchError(this.handleError)
        );
  }

  createUser(user: Partial<User>): Observable<User> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.post<User>(this.apiUrl, user, { headers })
        .pipe(
            tap(createdUser => console.log('User created:', createdUser)),
            catchError(this.handleError)
        );
  }

  updateUser(id: string, updates: Partial<User>): Observable<User> {
    if (this.useMockData) {
      return this.updateMockUser(id, updates);
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    

    // Converter o formato do frontend para o formato esperado pelo backend
    const updateRequest: UserUpdateRequest = {
      username: updates.username,
      name: updates.name,
      email: updates.email,
      phone: updates.phone,
      workload: updates.workload,
      status: updates.status,
      // Converter array de objetos Profile para array de IDs
      profileIds: updates.profiles?.map(p => p.id)
    };

    // Remover campos undefined/null
    Object.keys(updateRequest).forEach(key => {
      if (updateRequest[key as keyof UserUpdateRequest] === undefined) {
        delete updateRequest[key as keyof UserUpdateRequest];
      }
    });

    console.log('Sending update request:', updateRequest);

    return this.http.patch<User>(`${this.apiUrl}/${id}`, updateRequest, { headers })
        .pipe(
            tap(updatedUser => console.log('User updated:', updatedUser)),
            catchError(this.handleError)
        );
  }

  // Métodos para dados mockados
  private getMockUsers(page: number, size: number): Observable<PagedResult<User>> {
    const mockUsers: User[] = [
      {
        id: '550e8400-e29b-41d4-a716-446655440001',
        username: 'aelism',
        name: 'Antônio Elias de Melo',
        email: 'antonio@inf.ufg.br',
        phone: '62999999999',
        workload: 64,
        status: 'ACTIVE',
        profiles: [{ id: 4, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
      },
      {
        id: '550e8400-e29b-41d4-a716-446655440002',
        username: 'joao',
        name: 'João Batista',
        email: 'joao@inf.ufg.br',
        phone: '62988888888',
        workload: null,
        status: 'ACTIVE',
        profiles: [{ id: 9, role: 'DIRECTOR', description: 'Diretor', protectedProfile: true }]
      },
      {
        id: '550e8400-e29b-41d4-a716-446655440003',
        username: 'luteles',
        name: 'Luciana de Oliveira Teles',
        email: 'luteles@inf.ufg.br',
        phone: '62977777777',
        workload: 64,
        status: 'SUSPENDED',
        profiles: [{ id: 4, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
      }
    ];

    const start = page * size;
    const end = start + size;
    const content = mockUsers.slice(start, end);

    return of({
      content,
      totalElements: mockUsers.length,
      totalPages: Math.ceil(mockUsers.length / size),
      number: page,
      size
    });
  }

  private getMockUser(id: string): Observable<User> {
    const mockUsers: User[] = [
      {
        id: '550e8400-e29b-41d4-a716-446655440001',
        username: 'aelism',
        name: 'Antônio Elias de Melo',
        email: 'antonio@inf.ufg.br',
        phone: '62999999999',
        workload: 64,
        status: 'ACTIVE',
        profiles: [{ id: 4, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
      },
      {
        id: '550e8400-e29b-41d4-a716-446655440002',
        username: 'joao',
        name: 'João Batista',
        email: 'joao@inf.ufg.br',
        phone: '62988888888',
        workload: null,
        status: 'ACTIVE',
        profiles: [{ id: 9, role: 'DIRECTOR', description: 'Diretor', protectedProfile: true }]
      },
      {
        id: '550e8400-e29b-41d4-a716-446655440003',
        username: 'luteles',
        name: 'Luciana de Oliveira Teles',
        email: 'luteles@inf.ufg.br',
        phone: '62977777777',
        workload: 64,
        status: 'SUSPENDED',
        profiles: [{ id: 4, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
      }
    ];

    const user = mockUsers.find(u => u.id === id);
    return user ? of(user) : throwError(() => new Error('User not found'));
  }

  private updateMockUser(id: string, updates: Partial<User>): Observable<User> {
    return this.getMockUser(id).pipe(
        map(user => ({ ...user, ...updates }))
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error:', error);

    if (error.status === 0) {
      // Erro de CORS ou rede
      console.error('CORS or Network error. Check if backend is running and CORS is configured.');
      return throwError(() => new Error('Unable to connect to server. Please check if the backend is running.'));
    }

    if (error.error instanceof ErrorEvent) {
      // Erro do lado do cliente
      console.error('Client-side error:', error.error.message);
    } else {
      // Erro do servidor
      console.error(`Backend returned code ${error.status}, body was:`, error.error);
    }

    return throwError(() => new Error(error.error?.message || 'Something went wrong; please try again later.'));
  }

  // Método para alternar entre dados mockados e reais
  setUseMockData(useMock: boolean) {
    this.useMockData = useMock;
  }

}