import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-status-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './edit-status-modal.component.html',
  styleUrls: ['./edit-status-modal.component.scss']
})
export class EditStatusModalComponent {
  @Input() userName = '';
  @Input() selectedStatus: 'Ativo' | 'Suspenso' = 'Ativo';
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<'Ativo' | 'Suspenso'>();

  statusOptions: Array<'Ativo' | 'Suspenso'> = ['Ativo', 'Suspenso'];

  onSelectStatus(status: 'Ativo' | 'Suspenso') {
    this.selectedStatus = status;
  }

  onCancel() {
    this.close.emit();
  }

  onSave() {
    this.save.emit(this.selectedStatus);
  }
} 