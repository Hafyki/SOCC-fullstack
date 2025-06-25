import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

interface ProfileOption {
  label: string;
  value: string;
}

@Component({
  selector: 'app-edit-profile-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './edit-profile-modal.component.html',
  styleUrls: ['./edit-profile-modal.component.scss']
})
export class EditProfileModalComponent {
  @Input() userName = '';
  @Input() selectedProfiles: string[] = [];
  @Input() profileOptions: ProfileOption[] = [];
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<string[]>();

  onToggleProfile(profile: string) {
    if (this.selectedProfiles.includes(profile)) {
      this.selectedProfiles = this.selectedProfiles.filter(p => p !== profile);
    } else {
      this.selectedProfiles = [...this.selectedProfiles, profile];
    }
  }

  onCancel() {
    this.close.emit();
  }

  onSave() {
    this.save.emit(this.selectedProfiles);
  }
} 