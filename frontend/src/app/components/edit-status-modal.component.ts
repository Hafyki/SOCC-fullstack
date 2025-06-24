import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

interface StatusOption {
  label: string;
  value: 'ACTIVE' | 'SUSPENDED';
}

@Component({
  selector: 'app-edit-status-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './edit-status-modal.component.html',
  styleUrls: ['./edit-status-modal.component.scss']
})
export class EditStatusModalComponent {
  @Input() userName = '';
  @Input() selectedStatus: 'ACTIVE' | 'SUSPENDED' = 'ACTIVE';
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<'ACTIVE' | 'SUSPENDED'>();

  statusOptions: StatusOption[] = [
    { label: 'Ativo', value: 'ACTIVE' },
    { label: 'Suspenso', value: 'SUSPENDED' }
  ];

  onSelectStatus(status: 'ACTIVE' | 'SUSPENDED') {
    this.selectedStatus = status;
  }

  onCancel() {
    this.close.emit();
  }

  onSave() {
    this.save.emit(this.selectedStatus);
  }
} 