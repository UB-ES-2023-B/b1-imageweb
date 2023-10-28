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
  activeItem: string = 'gallery';

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
    //this.router.navigate(['/profile/edit']);
    console.log("not implemented yet")
  }

  ngOnInit(): void {
    this.getUserData();
  }
  handleItemClicked(item: string): void {
    this.activeItem = item;
    console.log('update var::', this.activeItem)
  }

  getUserData(): void {
      this.userService.getUser(this.user.name).subscribe(
        (response) => {
          this.user.email = response.body.userEmail;
          this.user.id = response.body.userId;
          console.log('Datos del usuario:', response.body);
        },
        (error) => {
          // Maneja el error aquí
          console.error('Error al obtener los datos del usuario', error);
        }
      );
    }
}
