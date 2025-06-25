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

  // Opções para modais - Atualizado com os IDs corretos do backend
  profileOptions = [
    { label: 'Administrador', value: 'ADMINISTRATOR' },
    { label: 'Coordenador de Curso', value: 'COURSE_COORDINATOR' },
    { label: 'Assistente de Coordenação', value: 'ASSISTANT_COURSE_COORDINATOR' },
    { label: 'Professor', value: 'PROFESSOR' },
    { label: 'Secretário Acadêmico', value: 'ACADEMIC_SECRETARY' },
    { label: 'Equipe Técnico-Pedagógica', value: 'TECHNICAL_PEDAGOGICAL_STAFF' },
    { label: 'Estudante', value: 'STUDENT' },
    { label: 'Curador Pedagógico', value: 'PEDAGOGICAL_CURATOR' },
    { label: 'Diretor', value: 'DIRECTOR' },
    { label: 'Vice-Diretor', value: 'DEPUTY_DIRECTOR' },
    { label: 'Membro do Conselho', value: 'BOARD_MEMBER' }
  ];

  // Mapeamento correto dos IDs baseado no DataSeeder
  private profileIdMap: { [key: string]: number } = {
    'ADMINISTRATOR': 1,
    'COURSE_COORDINATOR': 2,
    'ASSISTANT_COURSE_COORDINATOR': 3,
    'PROFESSOR': 4,
    'ACADEMIC_SECRETARY': 5,
    'TECHNICAL_PEDAGOGICAL_STAFF': 6,
    'STUDENT': 7,
    'PEDAGOGICAL_CURATOR': 8,
    'DIRECTOR': 9,
    'DEPUTY_DIRECTOR': 10,
    'BOARD_MEMBER': 11
  };

  constructor(private userService: UserService) {}

  ngOnInit() {
    console.log('UserTableComponent inicializado');
    this.loadUsers();
  }

  loadUsers(page = 0) {
    this.loading = true;
    this.error = null;

    console.log(`Carregando usuários - Página: ${page}, Tamanho: ${this.pageSize}`);

    this.userService.getUsers(page, this.pageSize, 'name', 'asc')
        .subscribe({
          next: (result) => {
            console.log('Resposta do backend:', result);
            console.log('Usuários recebidos:', result.content);
            console.log('Total de elementos:', result.totalElements);

            this.pagedResult = result;
            this.users = result.content;
            this.currentPage = result.number;
            this.totalElements = result.totalElements;
            this.totalPages = result.totalPages;
            this.loading = false;

            // Verificar se há usuários
            if (this.users.length === 0) {
              console.warn('Nenhum usuário foi retornado pelo backend');
            }
          },
          error: (error) => {
            this.error = 'Erro ao carregar usuários: ' + error.message;
            this.loading = false;
            console.error('Error loading users:', error);

            // Se for erro de conexão, sugerir usar dados mockados
            if (error.message.includes('Unable to connect')) {
              this.error += '. Considere ativar os dados mockados para desenvolvimento.';
            }
          }
        });
  }

  // Método para recarregar manualmente (útil para debug)
  reloadUsers() {
    console.log('Recarregando usuários manualmente...');
    this.loadUsers(this.currentPage);
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
      // Criar o objeto de atualização com os perfis selecionados
      const updatedUser: Partial<User> = {
        profiles: profiles.map(role => ({
          id: this.getProfileId(role),
          role: role as ProfileRole,
          description: this.getProfileDescription(role),
          protectedProfile: this.isProtectedProfile(role)
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
              console.log('Perfis atualizados com sucesso');
            },
            error: (error) => {
              console.error('Error updating user profiles:', error);
              alert('Erro ao atualizar perfis: ' + error.message);
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
              console.log('Status atualizado com sucesso');
            },
            error: (error) => {
              console.error('Error updating user status:', error);
              alert('Erro ao atualizar status: ' + error.message);
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
              console.log('Carga horária atualizada com sucesso');
            },
            error: (error) => {
              console.error('Error updating user workload:', error);
              alert('Erro ao atualizar carga horária: ' + error.message);
            }
          });
    }
  }

  // Métodos auxiliares
  private getProfileId(role: string): number {
    return this.profileIdMap[role] || 1;
  }

  private getProfileDescription(role: string): string {
    const descriptionMap: { [key: string]: string } = {
      'ADMINISTRATOR': 'Administrador do sistema com todos os acessos',
      'COURSE_COORDINATOR': 'Coordenador de curso',
      'ASSISTANT_COURSE_COORDINATOR': 'Coordenador assistente de curso',
      'PROFESSOR': 'Professor da instituição',
      'ACADEMIC_SECRETARY': 'Secretário acadêmico',
      'TECHNICAL_PEDAGOGICAL_STAFF': 'Equipe técnico-pedagógica',
      'STUDENT': 'Estudante da instituição',
      'PEDAGOGICAL_CURATOR': 'Curador pedagógico',
      'DIRECTOR': 'Diretor da instituição',
      'DEPUTY_DIRECTOR': 'Vice-diretor da instituição',
      'BOARD_MEMBER': 'Membro do conselho'
    };
    return descriptionMap[role] || role;
  }

  private isProtectedProfile(role: string): boolean {
    const protectedProfiles = [
      'ADMINISTRATOR',
      'COURSE_COORDINATOR',
      'ASSISTANT_COURSE_COORDINATOR',
      'ACADEMIC_SECRETARY',
      'DIRECTOR',
      'DEPUTY_DIRECTOR',
      'BOARD_MEMBER'
    ];
    return protectedProfiles.includes(role);
  }

  // Métodos para exibição na tabela
  getDisplayProfiles(user: User): string {
    if (!user.profiles || user.profiles.length === 0) {
      return 'Sem perfil';
    }
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