import { Component  } from '@angular/core';
import { GlobalDataService } from '../../services/global-data.service'
import {map, observeOn, Subscription} from 'rxjs';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  private usernameSubscription: Subscription = new Subscription();
  private profilePicSubscription: Subscription = new Subscription();
  username: string = '';
  profilePicUrl = this.globalDataService.getProfilePicture().previousUrl;


  constructor(private globalDataService:GlobalDataService, private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.usernameSubscription = this.globalDataService.username$.subscribe(username => {
      this.username = username;
    });
    this.profilePicSubscription = this.globalDataService.profilePicture$.subscribe(profilePhoto => {
      this.profilePicUrl = profilePhoto.previousUrl || '../assets/images/perfil.jpg';
    });
    this.getUserData();
  }

  ngOnDestroy() {
    this.usernameSubscription.unsubscribe();
    this.profilePicSubscription.unsubscribe();
  }

  private getUserData(): void {
    this.userService.getUser(this.globalDataService.getUsername()).subscribe(
      (response) => {
        if(response.body.profilePicture !== null) {
          this.profilePicUrl = `data:image/${response.body.profilePicture.photoName};base64,${response.body.profilePicture.data}`;
        }
      },
      (error) => {
        console.error('Error al obtener la foto de perfil', error);
      }
    );
  }

  goToProfile(itemActive:string): void {
    this.globalDataService.setActiveItem(itemActive);
    this.router.navigate(['/profile']);
  }

  logout() {
    this.globalDataService.clearSession();
    this.router.navigate(['/#']); // Redirige al usuario a la página de inicio
  }

  uploadImage(): void {
      console.log('Cargando imagen en proceso');
  }
}
