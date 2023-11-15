import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})

export class ChangePasswordComponent {
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';

  @Output() passwordChanged = new EventEmitter<any>();
  @Output() modalClosed = new EventEmitter<void>();

  constructor() {}

  changePassword(): void {
    // Lógica para cambiar la contraseña
    this.passwordChanged.emit({
      currentPassword: this.currentPassword,
      newPassword: this.newPassword,
      confirmPassword: this.confirmPassword
    });
    // Cierra el modal después de cambiar la contraseña
    this.modalClosed.emit();
  }
}
