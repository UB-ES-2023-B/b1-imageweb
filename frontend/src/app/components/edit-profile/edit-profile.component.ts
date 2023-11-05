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

  ngOnInit(): void {
    this.getUserData();
  }

  getUserData(): void {
    this.userService.getUser(this.user.name).subscribe(
      (response) => {
        this.user.email = response.body.userEmail;
        this.user.id = response.body.userId;
        if(response.body.profilePicture !== null) this.user.profilePicture = response.body.profilePicture;
      },
      (error) => {
        // Maneja el error aquí
        console.error('Error al obtener los datos del usuario', error);
      }
    );
  }

  actualizarPerfil() {

  }

  volver() {

  }

}
