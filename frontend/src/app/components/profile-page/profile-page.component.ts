import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})

export class ProfilePageComponent implements OnInit {
  activeItem: string = 'info';
  mostrarIconoLapiz: boolean = false; // Agregar una propiedad para controlar la visibilidad del ícono de lápiz

  user: any = {
    profilePicture: "../assets/images/perfil.jpg",
    coverPhoto: "../assets/images/mountainSea.jpg",
    name: this.globalDataService.getUsername(),

    email: '',
    id: 0,
    followers: [],
    following: [],
    photos: []
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
        if(response.body.profilePicture !== null) this.user.profilePicture = response.body.profilePicture;
        console.log('Datos del usuario:', response.body);
      },
      (error) => {
        // Maneja el error aquí
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
    if (inputElement) {
      inputElement.click();
    }
  }

// Método para cambiar la foto de perfil
  cambiarFotoPerfil(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        if (e.target) this.user.profilePicture = e.target.result as string;
      };
      reader.readAsDataURL(file);
      // Actualizar la base de datos u otras acciones necesarias
    }
  }






}
