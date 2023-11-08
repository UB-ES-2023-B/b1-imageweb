import { Component  } from '@angular/core';
import { GlobalDataService } from '../../services/global-data.service'
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

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


  constructor(private globalDataService:GlobalDataService, private router: Router) { }

  ngOnInit(): void {
    console.log(this.globalDataService.getProfilePicture().previousUrl);
    this.usernameSubscription = this.globalDataService.username$.subscribe(username => {
      this.username = username;
    });
    this.profilePicSubscription = this.globalDataService.profilePicture$.subscribe(profilePhoto => {
      this.profilePicUrl = profilePhoto.previousUrl || '../assets/images/perfil.jpg';
    });
  }

  ngOnDestroy() {
    this.usernameSubscription.unsubscribe();
    this.profilePicSubscription.unsubscribe();
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
