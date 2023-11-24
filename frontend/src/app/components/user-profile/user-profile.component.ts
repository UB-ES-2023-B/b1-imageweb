import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';
import { UserService } from '../../services/user.service';
import { GalleryService } from "../../services/gallery.service";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})

export class UserProfileComponent implements OnInit {
  activeItem: string = 'info';

  visited_user: any = {
    id: 0,
    name: '',
    profilePicture: null,
    profilePictureUrl: '../assets/images/perfil.jpg',
    coverPhoto: "../assets/images/cityLights.jpg",
    description: '',
    followers: [],
    following: []
  }

  constructor(private globalDataService: GlobalDataService,
              private router: Router,
              private userService: UserService,
              private galleryService: GalleryService,
              private toastr: ToastrService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.globalDataService.activeItem$.subscribe(newItem => { this.activeItem = newItem; });
    this.route.params.subscribe(params => {
      this.visited_user.id = params['id'];
      this.getVisitedUserData();
    });

  }

  private getVisitedUserData(): void {
    this.userService.getVisitedUser(this.visited_user.id).subscribe( //MODIFICAR
        (response) => {
          if (response.status === 200) {
            this.visited_user.name = response.body.username;
            this.visited_user.description = response.body.description;
            this.getVisitedUserProfilePicture();
            console.log('GET DATA VER PERFIL USER VISITED', response.body);
          }
          else {
            console.error('Error al obtener los datos del usuario', response);
            this.toastr.error("Error al obtener los datos del usuario.")
          }
        },
        (error) => {
          console.error('Error al obtener los datos del usuario', error);
        }
    );
  }

  handleItemClicked(item: string): void {
    this.activeItem = item;
  }

  private getVisitedUserProfilePicture(){
    this.userService.getVisitedUserProfilePhoto(this.visited_user.name).subscribe(
        (response) => {
          console.log("Profile pic:", response.body);
          if (response.status === 200) {
              if (response.body != null) {
                  this.visited_user.profilePicture = response.body;
                  this.visited_user.profilePictureUrl = `data:image/${response.body.photoName};base64,${response.body.data}`;
              }
              else this.visited_user.profilePictureUrl = "../../assets/images/perfil.jpg"
          }
          else {
            console.error('Error al obtener la foto de perfil del usuario', response);
            this.toastr.error("Error al obtener la foto de perfil del usuario");
          }
        },
        (error) => {
          console.error('Error al obtener la foto de perfil del usuario', error);
        }
    );
  }
}

