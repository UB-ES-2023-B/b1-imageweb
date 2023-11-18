import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})

export class EditProfileComponent implements OnInit {
  newUsername: string = '';
  newEmail: string = '';
  newDescription: string = '';
  showEmailError: boolean = false;
  showUserError: boolean = false;

  user: any = {
    id: 0,
    name: this.globalDataService.getUsername(),
    profilePicture: this.globalDataService.getProfilePicture().file,
    profilePictureUrl: this.globalDataService.getProfilePicture().previousUrl,
    email: this.globalDataService.getEmail(),
    description: this.globalDataService.getDescription()
  }

  constructor(private globalDataService: GlobalDataService,
              private router: Router,
              private userService: UserService,
              private toastr: ToastrService) {}

  ngOnInit(): void {
    this.getUserData();
  }

  private getUserData(): void {
    this.userService.getUser(this.user.name).subscribe(
      (response) => {
        this.user.email = response.body.userEmail;
        this.user.id = response.body.userId;
        this.user.description = response.body.description;
        if(response.body.profilePicture !== null) {
          this.user.profilePicture = response.body.profilePicture;
          this.user.profilePictureUrl = `data:image/${this.user.profilePicture.photoName};base64,${this.user.profilePicture.data}`;
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
              this.toastr.success('Foto de perfil cambiada satisfactoriamente');
            },
            (error) => {
              console.error('Error al actualizar la foto de perfil en el servidor', error);
              this.toastr.error('Error al subir la imagen');
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

  private eliminarFotoPerfil() {
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
      description: this.newDescription
    };

    if (this.showEmailError || this.showUserError)
      this.toastr.error("Campo del username y/o email incorrectos");
    else {
      this.userService.updateUser(this.user.name, updatedUser).subscribe(
        (response) => {
          console.log('Response:', response);
          if (response) { // CANVIAR AIXO PERQUE NOMES DETECTI RESPONSES CORRECTES (CAS DE LA DEMO DEL SPRINT 1)
            this.globalDataService.setUsername(this.newUsername);
            this.globalDataService.setEmail(this.newEmail);
            this.globalDataService.setDescription(this.newDescription);
            console.log('Usuario actualizado');
            this.toastr.success("Usuario actualizado con éxito")
            this.router.navigate(["/profile"]);
          }
        },
        (error) => {
          console.error('Error al actualizar el usuario', error);
          if (Object.keys(error.error)[0] === "Username already exists!")
            this.toastr.error("Este nombre de usuario ya está en uso.")
          else this.toastr.error("Error al actualizar el usuario")
        }
      );
    }
  }

  volver() {
    this.router.navigate(["/profile"]);
  }

  private urlTreatment(url: string) {
    const regex = /([^,]+),(.+)/;
    const match = url.match(regex);

    if (match && match.length === 3) return match[2];
    else console.log("No se encontró una coma en la URL.");
    return '';
  }

  onEmailChange() { this.showEmailError = false; } // Reset error message on email change

  onEmailBlur() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.showEmailError = !emailRegex.test(this.newEmail);
    console.log(this.showEmailError);
  }

  onUserChange() { this.showUserError = false; }  // Reset error message on username change

  onUserBlur() { if(this.newUsername == '') this.showUserError = true; }

}
