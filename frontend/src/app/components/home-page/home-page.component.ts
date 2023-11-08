import {Component} from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import {Subscription} from 'rxjs';
import {UserService} from 'src/app/services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  username: string = this.globalDataService.getUsername();

  constructor(private globalDataService: GlobalDataService,
              private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.username = this.globalDataService.getUsername();
    this.getUserData()


  }

  goToProfile(itemActive: string): void {
    this.globalDataService.setActiveItem(itemActive);
    this.router.navigate(['/profile']);
  }


  getUserData(): void {
    this.userService.getUser(this.username).subscribe(
      (response) => {
        this.globalDataService.setGalleryId(response.body.gallery.galleryrId);
        let url: string;
        if (response.body.profilePicture) url = `data:image/${response.body.profilePicture.photoName};base64,${response.body.profilePicture.data}`;
        else url = "../assets/images/perfil.jpg";
        this.globalDataService.setProfilePicture(response.body.profilePicture, url);
      },
      (error) => {
        // Maneja el error aquí
        console.error('Error al obtener los datos del usuario DESDE HOME', error);
      }
    );
  }

}
