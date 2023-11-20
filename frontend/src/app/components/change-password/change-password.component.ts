import { Component, EventEmitter, Output } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { UserService } from "../../services/user.service";
import {pass} from "ngx-bootstrap-icons";

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
  loading: boolean = false

  @Output() passwordChanged = new EventEmitter<any>();
  @Output() modalClosed = new EventEmitter<void>();

  constructor(private userService: UserService,
              private toastr: ToastrService) {}

  changePassword(): void {
    if (this.currentPassword === '' || this.newPassword === '' || this.confirmPassword === '')
      this.toastr.error("Rellene todos los campos")
    else if (this.showNewPasswordError || this.showConPasswordError)
      this.toastr.error("Comprueba que las contraseñas no tengan errores o que ambas coincidan");
    else if (this.currentPassword === this.newPassword)
      this.toastr.error("La nueva contraseña debe ser diferente a la anterior");
    else {
      this.loading = true;
      const passwords = {
        currentPassword: this.currentPassword,
        newPassword: this.newPassword
      };
      this.userService.changeUserPassword(passwords).subscribe(
        (response) => {
          console.log(Object.values(response)[0]);
          if (Object.values(response)[0] === 'Your password was changed successfully') {
            this.toastr.success("Contraseña cambiada correctamente");
            console.log('Contraseña actualizada', response, passwords);
            this.modalClosed.emit(); // Cierra el modal después de cambiar la contraseña
          }
        },
        (error) => {
          this.loading = false;
          console.error('Error al actualizar la contraseña', error);
          if (error.error) this.toastr.error("Contraseña actual errónea");
          else this.toastr.error("Error al actualizar la cotraseña");
        });
    }
  }

  onNewPasswordChange() {
    this.showNewPasswordError = false;
    if (this.newPassword == this.confirmPassword && this.confirmPassword !== '') this.showConPasswordError = false;
  }

  onNewPasswordBlur() {
    const passRegex = /^(?=.*[!@#$%^&*,.])((?=.*[A-Z])(?=.*[a-z])(?=.*\d)).{6,}$/;
    if (this.newPassword.length > 0) this.showNewPasswordError = !passRegex.test(this.newPassword);
    if (this.newPassword !== this.confirmPassword && this.confirmPassword !== '') this.showConPasswordError = true;
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
