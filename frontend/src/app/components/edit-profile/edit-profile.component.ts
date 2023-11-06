import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})

export class EditProfileComponent implements OnInit{
  newUsername: string = '';
  newEmail: string = '';

  user: any = {
    profilePicture: null,
    profilePictureUrl: '',
    name: this.globalDataService.getUsername(),
    email: '',
    id: 0
  }

  constructor(private globalDataService: GlobalDataService,
              private router: Router,
              private userService: UserService) {}

  ngOnInit(): void {
    this.getUserData();

  }

  getUserData(): void {
    this.userService.getUser(this.user.name).subscribe(
      (response) => {
        this.user.email = response.body.userEmail;
        this.user.id = response.body.userId;
        if(response.body.profilePicture !== null) {
          this.user.profilePicture = response.body.profilePicture;
          this.user.profilePictureUrl = `data:image/${this.user.profilePicture.photoName};base64,${this.user.profilePicture.data}`
        }
        this.newUsername = this.user.name;
        this.newEmail = response.body.userEmail;
        console.log(this.newUsername, this.newEmail);
      },
      (error) => {
        console.error('Error al obtener los datos del usuario', error);
      }
    );
  }

  actualizarFotoPerfil(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.addEventListener('load', (e: any) => {
        if (e.target) {
          // Llama al servicio para guardar la nueva foto de perfil en el servidor
          this.userService.setUserProfilePic(file).subscribe(
            (response) => {
              console.log('Foto de perfil actualizada con éxito en el servidor', response);
              this.globalDataService.setProfilePicture(file, `data:image/${file.name};base64,${this.urlTreatment(e.target.result as string)}`);
              this.user.profilePicture = this.globalDataService.getProfilePicture();
              this.user.profilePictureUrl = `data:image/${file.name};base64,${this.urlTreatment(e.target.result as string)}`;
            },
            (error) => {
              console.error('Error al actualizar la foto de perfil en el servidor', error);
            }
          );
        }
      });
      reader.readAsDataURL(file);
    }
  }

  borrarFotoPerfil() {
    this.userService.deleteUserProfilePhoto().subscribe(
      (response) => {
        console.log('Foto de perfil eliminada con éxito', response);
        this.eliminarFotoPerfil(); // Llama a la función en tu componente para limpiar la información de la foto de perfil
      },
      (error) => {
        console.error('Error al eliminar la foto de perfil', error);
      }
    );
  }

  eliminarFotoPerfil() {
    // Limpia la información de la foto de perfil en tu servicio GlobalDataService
    this.globalDataService.setProfilePicture(null, '');

    // Actualiza la propiedad user en tu componente para que muestre la imagen predeterminada
    this.user.profilePicture = null;
    this.user.profilePictureUrl = '../assets/images/perfil.jpg';
  }

  actualizarPerfil() {
    const updatedUser = {
      username: this.newUsername,
      email: this.newEmail,
    };

    this.userService.updateUser(this.user.name, updatedUser).subscribe(
      (response) => {
        if (response.status === 200) {
          console.log('Response:', response);
          this.globalDataService.setUsername(this.newUsername);
          this.globalDataService.setEmail(this.newEmail);
          this.globalDataService.setToken(response.body.token);
          console.log('Usuario actualizado');
          this.router.navigate(["/profile"])
        }
      },
      (error) => {
        // Maneja el error aquí
        console.error('Error al actualizar el usuario', error);
      }
    );
  }

  volver() {
    this.router.navigate(["/profile"]);
  }

  urlTreatment(url: string) {
    const regex = /([^,]+),(.+)/;
    const match = url.match(regex);

    if (match && match.length === 3) return match[2];
    else console.log("No se encontró una coma en la URL.");
    return '';
  }

}
