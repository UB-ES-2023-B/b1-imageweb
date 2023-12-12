import {Component} from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import {UserService} from 'src/app/services/user.service';
import {GalleryService} from "../../services/gallery.service";
import {MuroService} from "../../services/muro.service";
import {Router} from '@angular/router';
import {Lightbox} from "ngx-lightbox";

interface UserShown {
    username: string;
    avatar: string;
    ID: number;
    photos: any[];
}

interface photoShown {
    src: string,
    id: number,
    name: string,
    description: string
}

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})

export class HomePageComponent {
  username: string = this.globalDataService.getUsername();
  followingUsers: UserShown[] = [];
  loading: boolean = true;

  constructor(private globalDataService: GlobalDataService,
              private userService: UserService,
              private router: Router,
              private galleryService: GalleryService,
              private muroService: MuroService,
              private lightbox: Lightbox) {
  }

  ngOnInit(): void {
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

      this.loading = true;
      console.log("GFU inici", this.loading);
      this.muroService.getMuro().subscribe(
          (response) => {
              console.log("MURO", response.body)
              if (response.status === 200) {
                  for (const object of response.body) {
                      const existingUser = this.followingUsers.find((u) => u.username === object.userInfoDto.username);
                      if (existingUser) existingUser.photos.unshift(this.treatPhoto(object.photoDto));
                      else {
                          const newUser: UserShown = {
                              username: object.userInfoDto.username,
                              avatar: this.getUserAvatar(object.userInfoDto.profilePicture),
                              ID: object.userInfoDto.userId,
                              photos: [this.treatPhoto(object.photoDto)]
                          };

                          this.followingUsers.push(newUser);
                      }
                  }
              }
              this.stopLoadSpinner();
              console.log("GFU desactiva spin", this.loading);
          },
          (error) => {
              console.log("Error getting the muro", error);
              this.stopLoadSpinner();
          }
      );

      // let ids = [6,7,8,9,10,11,18,19,21,22];
      // for (let id of ids) {
      //     this.userService.getVisitedUser(id).subscribe(
      //         (response) => {
      //             if (response.status === 200) {
      //                 const followingUser: UserShown = {
      //                     ID: response.body.userId,
      //                     username: response.body.username,
      //                     avatar: this.getUserAvatar(response.body.profilePicture),
      //                     photos: this.getUserPhotos(response.body.username)
      //                 }
      //                 if (this.globalDataService.getUsername() !== followingUser.username) this.followingUsers.push(followingUser);
      //             }
      //         },
      //         (error) => {
      //             console.error(`Error al obtener el user con id ${id}`, error);
      //         }
      //     );
      // }
      console.log("GFU final", this.loading);
  }

  private treatPhoto(photo: any): photoShown {
      return {
          src: `data:image/${photo.photoExtensio};base64,${photo.data}`,
          id: photo.photoId,
          name: photo.photoName,
          description: photo.photoDescription
      };
  }

  private getUserAvatar(profilePic: any): string {
      if (profilePic === null) return '../../assets/images/perfil.jpg'
      else {
          return `data:image/${profilePic.photoExtensio};base64,${profilePic.data}`;
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
      let user_found = this.followingUsers.find((element) => element.username === user.username)
      if (user_found) {
          let user_pics = user_found.photos
          let photo_index = user_pics.indexOf(user_pics.find((pic: { id: number; }) => pic.id === photoId))
          this.lightbox.open(user_pics, photo_index)
      }

  }

  goToUserProfile(id: number) {
      console.log("ID USER", id);
      this.router.navigate([`/user-profile/${id}`]);
  }

  stopLoadSpinner() { console.log("PARA DE CARGAR WEON"); this.loading = false; }
}
