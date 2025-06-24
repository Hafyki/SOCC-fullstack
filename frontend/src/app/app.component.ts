import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { TopBarComponent } from './components/top-bar.component';
import { SideMenuComponent } from './components/side-menu.component';
import { UserTableComponent } from './components/user-table.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, TopBarComponent, SideMenuComponent, UserTableComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'SOCC Frontend';
  message = 'Hello World!';
  currentTime = new Date();
  
  constructor() {
    // Atualizar o horário a cada segundo
    setInterval(() => {
      this.currentTime = new Date();
    }, 1000);
  }
} 