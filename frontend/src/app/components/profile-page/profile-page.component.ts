import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  user: any = {}

  constructor(private globalDataService: GlobalDataService,
              private router: Router) {}

  editProfile() {
    // Redirige al usuario a la página de edición de perfil
    this.router.navigate(['/profile/edit']);
  }

  ngOnInit(): void {
    this.user = {
      profilePicture: "../assets/images/perfil.jpg",
      coverPhoto: "../assets/images/mountainSea.jpg",
      name: this.globalDataService.getUsername(),
      email: this.globalDataService.getEmail(),
      followers: [],
      following: [],
      photos: []
    };
  }

}
