import {Component} from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import {Subscription} from 'rxjs';
import {UserService} from 'src/app/services/user.service';
import {GalleryService} from "../../services/gallery.service";
import {Router} from '@angular/router';
import {Lightbox} from "ngx-lightbox";

interface UserShown {
    username: string;
    avatar: string;
    ID: number;
    photos: any[];
}

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  username: string = this.globalDataService.getUsername();
  followingUsers: any[] = [];
  loading: boolean = true;

  constructor(private globalDataService: GlobalDataService,
              private userService: UserService,
              private router: Router,
              private galleryService: GalleryService,
              private lightbox: Lightbox) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.username = this.globalDataService.getUsername();
    this.getUserData();
    this.getFollowingUsers();
    console.log('USERS GOT', this.followingUsers);
  }

  private getUserData(): void {
    this.userService.getUser(this.username).subscribe(
      (response) => {
        console.log('GET DATA HOME ', response.body);
        this.globalDataService.setEmail(response.body.email);
        this.globalDataService.setDescription(response.body.description);
        this.globalDataService.setGalleryId(response.body.gallery.galleryrId);
        console.log('Este es el id de la galeria: (back) ',response.body.gallery.galleryrId)
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

  private getFollowingUsers(): void {
      // GET FOLLOWING USERS DUMMY (FOTO DE PERFIL AMB DATA PLS)
      let ids = [6,7,8,9,10,11,18,19,21,22];
      for (let id of ids) {
          this.userService.getVisitedUser(id).subscribe(
              (response) => {
                  if (response.status === 200) {
                      const followingUser: UserShown = {
                          ID: response.body.userId,
                          username: response.body.username,
                          avatar: this.getUserAvatar(response.body.profilePicture),
                          photos: this.getUserPhotos(response.body.username)
                      }
                      if (this.globalDataService.getUsername() !== followingUser.username) this.followingUsers.push(followingUser);
                  }
              },
              (error) => {
                  console.error(`Error al obtener el user con id ${id}`, error);
              }
          );
      }
      this.stopLoadSpinner()
  }

  private getUserAvatar(profilePic: any): string {
      if (profilePic === null) return '../../assets/images/perfil.jpg'
      else {
          return '../../assets/images/logo2.png';
      }
  }

  private getUserPhotos(username: string): any[] {
      let photos: any[] = [];
      this.galleryService.getGalleryUser(username).subscribe(
          (response)=> {
              if (response.status === 200 && response.body && Array.isArray(response.body)) {
                  response.body.forEach((element: any) => {
                      if (element.data) {
                          photos.unshift({
                              "src": `data:image/${element.photoExtensio};base64,${element.data}`,
                              "id": element.photoId,
                              "name": element.photoName,
                              "description": element.photoDescription
                          });
                      }
                  });
              }
          },
          (error) => {
              console.error(`Error al obtener galeria del usuario ${username}`, error)
          }
      );
      return photos;
  }

  openPic(user: any, photoId: number) {
      let user_pics = this.followingUsers.find((element) => element.username === user.username).photos
      let photo_found = user_pics.find((pic: { id: number; }) => pic.id === photoId)
      let photo_index = user_pics.indexOf(photo_found)
      this.lightbox.open(user_pics, photo_index)
  }

  goToUserProfile(id: number) {
      console.log("ID USER", id);
      this.router.navigate([`/user-profile/${id}`]);
  }

  stopLoadSpinner() { console.log("PARA DE CARGAR WEON"); this.loading = false; }
}
