import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  constructor(private router: Router) {}

   user: any = {
      profilePicture: "../assets/images/funnyCat.jpg",
      coverPhoto: "../assets/images/mountainSea.jpg",
      name: 'Nombre del Usuario',
      email: 'correo@ejemplo.com',
      photos: [
        { url: 'URL_DE_LA_FOTO', description: 'Descripción de la foto 1' },
        { url: 'URL_DE_LA_FOTO', description: 'Descripción de la foto 2' },
        // ... más fotos
      ]
    };

  editProfile() {
    // Redirige al usuario a la página de edición de perfil
    this.router.navigate(['/profile/edit']);
  }

  ngOnInit(): void {
    // Obtener datos reales del usuario y sus fotos desde la API
  }

}
