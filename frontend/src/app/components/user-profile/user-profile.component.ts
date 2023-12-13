import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalDataService } from '../../services/global-data.service';
import { UserService } from '../../services/user.service';
import { GalleryService } from "../../services/gallery.service";
import { ToastrService } from 'ngx-toastr';
import { FollowersService } from "../../services/followers.service";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})

export class UserProfileComponent implements OnInit {
  activeItem: string = 'info';
  loadingFollowers = false; //
  ownerUser: string = this.globalDataService.getUsername();
  follow: boolean = false;
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
  modalTitle: string = ''
  charging: boolean = false
  infoModal: any = [];
  loading = false
  message: string = ''

  constructor(private globalDataService: GlobalDataService,
    private router: Router,
    private userService: UserService,
    private galleryService: GalleryService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private followersService: FollowersService,
    private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.globalDataService.activeItem$.subscribe(newItem => { this.activeItem = newItem; });
    this.route.params.subscribe(params => {
      this.visited_user.id = params['id'];
      this.activeItem = 'info';
      this.getVisitedUserData();

    });


  }

  private getVisitedUserData(): void {
    this.loadingFollowers = false;
    this.userService.getVisitedUser(this.visited_user.id).subscribe(
      (response) => {
        if (response.status === 200) {
          this.visited_user.name = response.body.username;
          this.visited_user.description = response.body.description;
          this.getVisitedUserProfilePicture();
          console.log('GET DATA VER PERFIL USER VISITED', response.body);
          this.getFollowers(this.visited_user.name, true)
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

  getFollowings(username: string) {
    this.visited_user.following = [];
    this.followersService.getFollowing(username).subscribe(
      (followingResponse) => {
        if (followingResponse.body) {
          const following = followingResponse.body.following;
          following.forEach((usuario: any) => {
            this.visited_user.following.push(usuario);
          });
          this.loadingFollowers = true;
        }
      },
      (followingError) => {
        console.log('HAY UN ERROR EN LOS SEGUIDOS', followingError);
      }
    );
  }

  getFollowers(username: string, all: boolean = false) {
    this.follow = false;
    this.visited_user.followers = [];
    this.followersService.getFollowers(username).subscribe(
      (response) => {
        if (response.body) {
          response.body.followers.forEach((usuario: any) => {
            if (this.ownerUser == usuario.username) {
              this.follow = true;
            }
            this.visited_user.followers.push(usuario);
          });
          if (all) {
            this.getFollowings(username);
          } else {
            this.charging = false;
          }

        }
      }, (error) => {
        console.log('HAY UN ERROR EN SEGUIDORES', error)
      }
    )
  }

  followUser(username: string) {
    this.charging = true
    this.followersService.followUser(username).subscribe(
      (response) => {
        console.log('eyyy response::', response)
        this.toastr.success("Ahora sigues a " + username);
        this.getFollowers(this.visited_user.name)
      },
      (error) => {
        console.error('Error al seguir al usuario', error);
        this.toastr.error("Error al seguir al usuario.")
      }
    );
  }
  openModal(content: any, type: string) {
    if (type == 'followers') {
      this.modalTitle = 'Lista de seguidores'
      this.infoModal = this.visited_user.followers
      this.message = 'Este usuario no tiene seguidores'

    } else {
      this.modalTitle = 'Lista de usuarios seguidos'
      this.infoModal = this.visited_user.following
      this.message = 'Este usuario no sigue a nadie'
    }

    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title', size: 'lg' }).result.then((result) => {
      console.log(result)

    }, (reason) => {
      console.log(reason)
    });
  }
  unfollow(username: string) {
    this.charging = true
    this.followersService.unfollowUser(username).subscribe(
      (response) => {

        this.toastr.success("Dejaste de seguir a " + username);
        this.getFollowers(this.visited_user.name)

      },
      (error) => {
        console.error('Error al dejar de seguir al usuario', error);
        this.toastr.error("Error al dejar de seguir al usuario.")

      }
    );
  }
  private getVisitedUserProfilePicture() {
    this.userService.getVisitedUserProfilePhoto(this.visited_user.name).subscribe(
      (response) => {
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

