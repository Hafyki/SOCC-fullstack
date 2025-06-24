import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { User, Profile } from '../models/user.model';

export interface PagedResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  // URL base da API - ajuste conforme necessário
  private readonly apiUrl = 'http://localhost:8080/users';
  
  // Flag para controlar se deve usar dados mockados
  private useMockData = true;

  constructor(private http: HttpClient) {}

  getUsers(page = 0, size = 10, sortBy = 'name', sortDir = 'asc'): Observable<PagedResult<User>> {
    if (this.useMockData) {
      return this.getMockUsers(page, size);
    }

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
    
    return this.http.get<PagedResult<User>>(this.apiUrl, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

  getUser(id: string): Observable<User> {
    if (this.useMockData) {
      return this.getMockUser(id);
    }

    return this.http.get<User>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  createUser(user: Partial<User>): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  updateUser(id: string, user: Partial<User>): Observable<User> {
    if (this.useMockData) {
      return this.updateMockUser(id, user);
    }

    return this.http.patch<User>(`${this.apiUrl}/${id}`, user)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Métodos para dados mockados
  private getMockUsers(page: number, size: number): Observable<PagedResult<User>> {
    const mockUsers: User[] = [
      {
        id: '1',
        username: 'aelism',
        name: 'Antônio Elias de Melo',
        email: 'antonio@inf.ufg.br',
        phone: '62999999999',
        workload: 64,
        status: 'ACTIVE',
        profiles: [{ id: 1, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
      },
      {
        id: '2',
        username: 'joao',
        name: 'João Batista',
        email: 'joao@inf.ufg.br',
        phone: '62988888888',
        workload: null,
        status: 'ACTIVE',
        profiles: [{ id: 2, role: 'DIRECTOR', description: 'Diretor', protectedProfile: true }]
      },
      {
        id: '3',
        username: 'luteles',
        name: 'Luciana de Oliveira Teles',
        email: 'luteles@inf.ufg.br',
        phone: '62977777777',
        workload: 64,
        status: 'SUSPENDED',
        profiles: [{ id: 1, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
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
        id: '1',
        username: 'aelism',
        name: 'Antônio Elias de Melo',
        email: 'antonio@inf.ufg.br',
        phone: '62999999999',
        workload: 64,
        status: 'ACTIVE',
        profiles: [{ id: 1, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
      },
      {
        id: '2',
        username: 'joao',
        name: 'João Batista',
        email: 'joao@inf.ufg.br',
        phone: '62988888888',
        workload: null,
        status: 'ACTIVE',
        profiles: [{ id: 2, role: 'DIRECTOR', description: 'Diretor', protectedProfile: true }]
      },
      {
        id: '3',
        username: 'luteles',
        name: 'Luciana de Oliveira Teles',
        email: 'luteles@inf.ufg.br',
        phone: '62977777777',
        workload: 64,
        status: 'SUSPENDED',
        profiles: [{ id: 1, role: 'PROFESSOR', description: 'Professor', protectedProfile: false }]
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
    console.error('An error occurred:', error);
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }

  // Método para alternar entre dados mockados e reais
  setUseMockData(useMock: boolean) {
    this.useMockData = useMock;
  }
} 