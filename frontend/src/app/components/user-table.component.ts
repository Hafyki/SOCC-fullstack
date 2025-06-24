import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditProfileModalComponent } from './edit-profile-modal.component';
import { EditStatusModalComponent } from './edit-status-modal.component';
import { EditChModalComponent } from './edit-ch-modal.component';

interface User {
  username: string;
  name: string;
  email: string;
  profile: string[];
  minCH: number | null;
  status: 'Ativo' | 'Suspenso';
}

@Component({
  selector: 'app-user-table',
  standalone: true,
  imports: [CommonModule, EditProfileModalComponent, EditStatusModalComponent, EditChModalComponent],
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.scss']
})
export class UserTableComponent {
  users: User[] = [
    {
      username: 'aelism',
      name: 'Antônio Elias de Melo',
      email: 'antonio@inf.ufg.br',
      profile: ['DOCENTE'],
      minCH: 64,
      status: 'Ativo'
    },
    {
      username: 'joao',
      name: 'João Batista',
      email: 'joao@inf.ufg.br',
      profile: ['DIRETOR'],
      minCH: null,
      status: 'Ativo'
    },
    {
      username: 'luteles',
      name: 'Luciana de Oliveira Teles',
      email: 'luteles@inf.ufg.br',
      profile: ['DOCENTE'],
      minCH: 64,
      status: 'Suspenso'
    }
  ];

  profileOptions = [
    { label: 'DOCENTE', value: 'DOCENTE' },
    { label: 'VICE-DIRETOR(A)', value: 'VICE-DIRETOR(A)' }
  ];

  showProfileModal = false;
  selectedUser: User | null = null;
  selectedProfiles: string[] = [];

  showStatusModal = false;
  selectedStatus: 'Ativo' | 'Suspenso' = 'Ativo';

  showChModal = false;
  selectedCh: number | null = null;

  openProfileModal(user: User) {
    this.selectedUser = user;
    this.selectedProfiles = [...user.profile];
    this.showProfileModal = true;
  }

  closeProfileModal() {
    this.showProfileModal = false;
    this.selectedUser = null;
    this.selectedProfiles = [];
  }

  saveProfileModal(profiles: string[]) {
    if (this.selectedUser) {
      this.selectedUser.profile = profiles;
    }
    this.closeProfileModal();
  }

  openStatusModal(user: User) {
    this.selectedUser = user;
    this.selectedStatus = user.status;
    this.showStatusModal = true;
  }

  closeStatusModal() {
    this.showStatusModal = false;
    this.selectedUser = null;
  }

  saveStatusModal(status: 'Ativo' | 'Suspenso') {
    if (this.selectedUser) {
      this.selectedUser.status = status;
    }
    this.closeStatusModal();
  }

  openChModal(user: User) {
    this.selectedUser = user;
    this.selectedCh = user.minCH;
    this.showChModal = true;
  }

  closeChModal() {
    this.showChModal = false;
    this.selectedUser = null;
    this.selectedCh = null;
  }

  saveChModal(ch: number) {
    if (this.selectedUser) {
      this.selectedUser.minCH = ch;
    }
    this.closeChModal();
  }
} 