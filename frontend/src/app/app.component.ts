import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
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