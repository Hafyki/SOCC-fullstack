import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditProfileModalComponent } from './edit-profile-modal.component';
import { EditStatusModalComponent } from './edit-status-modal.component';
import { EditChModalComponent } from './edit-ch-modal.component';
import { UserService, PagedResult } from '../services/user.service';
import { User, ProfileRole } from '../models/user.model';

@Component({
  selector: 'app-user-table',
  standalone: true,
  imports: [CommonModule, EditProfileModalComponent, EditStatusModalComponent, EditChModalComponent],
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.scss']
})
export class UserTableComponent implements OnInit {
  users: User[] = [];
  pagedResult: PagedResult<User> | null = null;
  loading = false;
  error: string | null = null;

  // Paginação
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Modais
  showProfileModal = false;
  showStatusModal = false;
  showChModal = false;
  selectedUser: User | null = null;
  selectedProfiles: string[] = [];
  selectedStatus: 'ACTIVE' | 'SUSPENDED' = 'ACTIVE';
  selectedCh: number | null = null;

  // Opções para modais
  profileOptions = [
    { label: 'Professor', value: 'PROFESSOR' },
    { label: 'Diretor', value: 'DIRECTOR' },
    { label: 'Vice-Diretor', value: 'DEPUTY_DIRECTOR' },
    { label: 'Coordenador de Curso', value: 'COURSE_COORDINATOR' },
    { label: 'Assistente de Coordenação', value: 'ASSISTANT_COURSE_COORDINATOR' }
  ];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers(page = 0) {
    this.loading = true;
    this.error = null;
    
    this.userService.getUsers(page, this.pageSize, 'name', 'asc')
      .subscribe({
        next: (result) => {
          this.pagedResult = result;
          this.users = result.content;
          this.currentPage = result.number;
          this.totalElements = result.totalElements;
          this.totalPages = result.totalPages;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Erro ao carregar usuários';
          this.loading = false;
          console.error('Error loading users:', error);
        }
      });
  }

  // Métodos de paginação
  onPageChange(page: number) {
    this.loadUsers(page);
  }

  onPageSizeChange(size: string) {
    this.pageSize = parseInt(size, 10);
    this.loadUsers(0);
  }

  // Método auxiliar para substituir Math.min no template
  getMinValue(a: number, b: number): number {
    return Math.min(a, b);
  }

  // Métodos do modal de perfil
  openProfileModal(user: User) {
    this.selectedUser = user;
    this.selectedProfiles = user.profiles.map(p => p.role);
    this.showProfileModal = true;
  }

  closeProfileModal() {
    this.showProfileModal = false;
    this.selectedUser = null;
    this.selectedProfiles = [];
  }

  saveProfileModal(profiles: string[]) {
    if (this.selectedUser) {
      const updatedUser = {
        ...this.selectedUser,
        profiles: profiles.map(role => ({
          id: this.getProfileId(role),
          role: role as ProfileRole,
          description: this.getProfileDescription(role),
          protectedProfile: false
        }))
      };

      this.userService.updateUser(this.selectedUser.id, updatedUser)
        .subscribe({
          next: (updatedUser) => {
            // Atualizar o usuário na lista local
            const index = this.users.findIndex(u => u.id === updatedUser.id);
            if (index !== -1) {
              this.users[index] = updatedUser;
            }
            this.closeProfileModal();
          },
          error: (error) => {
            console.error('Error updating user profiles:', error);
            // Aqui você pode adicionar um toast ou notificação de erro
          }
        });
    }
  }

  // Métodos do modal de status
  openStatusModal(user: User) {
    this.selectedUser = user;
    this.selectedStatus = user.status;
    this.showStatusModal = true;
  }

  closeStatusModal() {
    this.showStatusModal = false;
    this.selectedUser = null;
  }

  saveStatusModal(status: 'ACTIVE' | 'SUSPENDED') {
    if (this.selectedUser) {
      this.userService.updateUser(this.selectedUser.id, { status })
        .subscribe({
          next: (updatedUser) => {
            const index = this.users.findIndex(u => u.id === updatedUser.id);
            if (index !== -1) {
              this.users[index] = updatedUser;
            }
            this.closeStatusModal();
          },
          error: (error) => {
            console.error('Error updating user status:', error);
          }
        });
    }
  }

  // Métodos do modal de carga horária
  openChModal(user: User) {
    this.selectedUser = user;
    this.selectedCh = user.workload;
    this.showChModal = true;
  }

  closeChModal() {
    this.showChModal = false;
    this.selectedUser = null;
    this.selectedCh = null;
  }

  saveChModal(workload: number) {
    if (this.selectedUser) {
      this.userService.updateUser(this.selectedUser.id, { workload })
        .subscribe({
          next: (updatedUser) => {
            const index = this.users.findIndex(u => u.id === updatedUser.id);
            if (index !== -1) {
              this.users[index] = updatedUser;
            }
            this.closeChModal();
          },
          error: (error) => {
            console.error('Error updating user workload:', error);
          }
        });
    }
  }

  // Métodos auxiliares
  private getProfileId(role: string): number {
    const profileMap: { [key: string]: number } = {
      'PROFESSOR': 1,
      'DIRECTOR': 2,
      'DEPUTY_DIRECTOR': 3,
      'COURSE_COORDINATOR': 4,
      'ASSISTANT_COURSE_COORDINATOR': 5
    };
    return profileMap[role] || 1;
  }

  private getProfileDescription(role: string): string {
    const descriptionMap: { [key: string]: string } = {
      'PROFESSOR': 'Professor',
      'DIRECTOR': 'Diretor',
      'DEPUTY_DIRECTOR': 'Vice-Diretor',
      'COURSE_COORDINATOR': 'Coordenador de Curso',
      'ASSISTANT_COURSE_COORDINATOR': 'Assistente de Coordenação'
    };
    return descriptionMap[role] || role;
  }

  // Métodos para exibição na tabela
  getDisplayProfiles(user: User): string {
    return user.profiles.map(p => p.description).join(', ');
  }

  getDisplayStatus(user: User): string {
    return user.status === 'ACTIVE' ? 'Ativo' : 'Suspenso';
  }

  getStatusClass(user: User): string {
    return user.status === 'ACTIVE' ? 'ativo' : 'suspenso';
  }

  // Cálculo do CH Total
  getTotalWorkload(): number {
    return this.users.reduce((total, user) => total + (user.workload || 0), 0);
  }
} 