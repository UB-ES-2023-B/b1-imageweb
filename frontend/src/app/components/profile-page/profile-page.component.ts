import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';
import {type} from "ngx-bootstrap-icons";

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})

export class ProfilePageComponent implements OnInit {
  activeItem: string = 'info';
  mostrarIconoLapiz: boolean = false;

  user: any = {
    profilePicture: null,
    profilePictureUrl: '',
    coverPhoto: "../assets/images/mountainSea.jpg",
    name: this.globalDataService.getUsername(),
    email: '',
    id: 0,
    followers: [],
    following: []
  }

  constructor(private globalDataService: GlobalDataService,
              private router: Router,
              private userService: UserService) {}

  editProfile() : void {
    // Redirige al usuario a la página de edición de perfil
    this.router.navigate(['/profile/edit']);
  }

  ngOnInit(): void {
    this.globalDataService.activeItem$.subscribe(newItem => {
      this.activeItem = newItem;
    });
    this.getUserData();
  }

  handleItemClicked(item: string): void {
    this.activeItem = item;
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
        console.log('Datos del usuario:', response.body);
      },
      (error) => {
        console.error('Error al obtener los datos del usuario', error);
      }
    );
  }

  // Método para mostrar el ícono de lápiz al hacer hover en la foto de perfil
  mostrarEditarIcono(): void {
    this.mostrarIconoLapiz = true;
  }

// Método para ocultar el ícono de lápiz cuando se quita el hover de la foto de perfil
  ocultarEditarIcono(): void {
    this.mostrarIconoLapiz = false;
  }

// Método para abrir el cuadro de diálogo de selección de archivo
  abrirInputFile(): void {
    const inputElement = document.getElementById('fileInput');
    if (inputElement) inputElement.click();
  }

// Método para cambiar la foto de perfil
  cambiarFotoPerfil(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.addEventListener('load', (e: any) => {
        if (e.target) {
          // Llama al servicio para guardar la nueva foto de perfil en el servidor
          this.userService.setUserProfilePic(file).subscribe(
            (response) => {
              console.log('Foto de perfil actualizada con éxito en el servidor', response);
              this.globalDataService.setProfilePicture(file);
              this.user.profilePicture = this.globalDataService.getProfilePicture();
              this.user.profilePictureUrl = `data:image/${file.name};base64,${this.urlTreatment(e.target.result as string)}`;
              console.log(this.user.profilePictureUrl)
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

  urlTreatment(url: string) {
    const regex = /([^,]+),(.+)/;
    const match = url.match(regex);

    if (match && match.length === 3) return match[2];
    else console.log("No se encontró una coma en la URL.");
    return '';
  }
}
