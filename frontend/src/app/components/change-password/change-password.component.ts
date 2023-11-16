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
  showNewPasswordError: boolean = false;
  showConPasswordError: boolean = false;
  showOldPassword: boolean = false;
  showNewPassword: boolean = false;
  showConPassword: boolean = false;

  @Output() passwordChanged = new EventEmitter<any>();
  @Output() modalClosed = new EventEmitter<void>();

  constructor() {}

  changePassword(): void {
    if (this.showNewPasswordError && this.showConPasswordError) {
      // ERROR
    }
    else {
      // COMPARAR CONTRASENYES ANTIGUES
      // SPINNER DE CARREGANT
      // CANVIAR DADES A BD
      // CONFIRMACIÓ
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

  onNewPasswordChange() {
    this.showNewPasswordError = false;
  }

  onNewPasswordBlur() {
    const passRegex = /^(?=.*[!@#$%^&*,.])((?=.*[A-Z])(?=.*[a-z])(?=.*\d)).{6,}$/;
    if (this.newPassword.length > 0) this.showNewPasswordError = !passRegex.test(this.newPassword);
  }

  onConPasswordChange() {
    this.showConPasswordError = false;  // Reset error message on password change
  }

  onConPasswordBlur() {
    if(this.newPassword != this.confirmPassword){
      this.showConPasswordError = true;
    }
  }

  toggleOldPasswordVisibility() {
    this.showOldPassword = !this.showOldPassword;
  }

  toggleNewPasswordVisibility() {
    this.showNewPassword = !this.showNewPassword;
  }

  toggleConPasswordVisibility() {
    this.showConPassword = !this.showConPassword;
  }
}
