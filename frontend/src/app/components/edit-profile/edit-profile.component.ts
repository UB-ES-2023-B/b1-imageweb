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
  newProfilePic: string = '';

  user: any = {
    profilePicture: "../assets/images/perfil.jpg",
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
        if(response.body.profilePicture !== null) this.user.profilePicture = response.body.profilePicture;
        this.newUsername = this.user.name;
        this.newEmail = response.body.userEmail;
        console.log(this.newUsername, this.newEmail);
      },
      (error) => {
        // Maneja el error aquí
        console.error('Error al obtener los datos del usuario', error);
      }
    );
  }

  actualizarFotoPerfil(event: any) {
    const file: File = event.target.files[0];
    let picture: string;
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        if (e.target) {
          this.user.profilePicture = e.target.result as string;
          this.newProfilePic = e.target.result as string;
        }
      };
      reader.readAsDataURL(file);
    }
  }

  borrarFotoPerfil() {
    this.user.profilePicture = "../assets/images/perfil.jpg";
    this.newProfilePic ="../assets/images/perfil.jpg";
  }

  actualizarPerfil() {
    const updatedUser = {
      username: this.newUsername,
      email: this.newEmail,
      profilePicture: this.newProfilePic
    };

    this.userService.updateUser(this.user.name, updatedUser).subscribe(
      (response) => {
        if (response.status === 200) {
          console.log('Response:', response);
          this.globalDataService.setUsername(this.newUsername);
          this.globalDataService.setEmail(this.newEmail);
          this.globalDataService.setProfilePicture(this.newProfilePic);
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

}
