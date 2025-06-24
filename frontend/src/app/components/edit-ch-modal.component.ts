import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-ch-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-ch-modal.component.html',
  styleUrls: ['./edit-ch-modal.component.scss']
})
export class EditChModalComponent {
  @Input() chValue: number | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<number>();

  onCancel() {
    this.close.emit();
  }

  onSave() {
    if (this.chValue !== null) {
      this.save.emit(this.chValue);
    }
  }
} 