import { Component } from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  username: string = '';

  constructor(private globalDataService:GlobalDataService,
    private userService:UserService) { }

  ngOnInit(): void {
    this.username = this.globalDataService.getUsername();
    this.getUserData()


  }


  getUserData(): void {
    this.userService.getUser(this.username).subscribe(
      (response) => {
        this.globalDataService.setGalleryId(response.body.gallery.galleryrId);
      },
      (error) => {
        // Maneja el error aquí
        console.error('Error al obtener los datos del usuario DESDE HOME', error);
      }
    );
  }

}
