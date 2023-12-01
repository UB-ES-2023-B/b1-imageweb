import { Component, Input } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Subscription} from 'rxjs';
import {AlbumsService} from '../../services/albums.service';
import {GlobalDataService} from 'src/app/services/global-data.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';


@Component({
  selector: 'app-edit-albums',
  templateUrl: './edit-albums.component.html',
  styleUrls: ['./edit-albums.component.css']
})
export class EditAlbumsComponent {
  original_username: string = this.globalDataService.getUsername();


  modalTitle: string = 'Nuevo álbum'
  name: string = '';
  description: string = '';
  remainingWords: number = 25;
  loading: boolean = true;
  albums: any[] = [];

  private albumsSubscription: Subscription = new Subscription();

  constructor(private modalService: NgbModal,
              private albumsService: AlbumsService,
              private toastr: ToastrService,
              private router: Router,
              private globalDataService: GlobalDataService) {
  }


  ngOnInit(): void {
      this.albumsSubscription = this.albumsService.getAlbumsObservable().subscribe((albums) => {
          this.albums = albums;
      });
      this.getAlbums();
  }

  getAlbums() {
      this.albums = []
      this.albumsService.getAlbumsForUser(this.globalDataService.getUsername()).subscribe(
          (response) => {
              if (response.body && Array.isArray(response.body.albums)) {
                  response.body.albums.forEach((element: any) => {
                      let src;
                      if (element.length > 0) {
                          if (element.length > 1) {
                              src = `data:image/${element[1].photoExtension};base64,${element[1].data}`;
                          } else {
                              src = '../../../assets/images/defaultImageAlbum.jpg';
                          }
                          this.albums.unshift({
                              "src": src,
                              "id": element[0].album.albumId,
                              "name": element[0].album.albumName,
                              "description": element[0].album.description,
                              "photoLength": element.length - 1
                          });
                      }
                  });
              }
              this.albumsService.setAlbums(this.albums);
              this.loading = false;
          },
          (error) => {
              console.log('error al obtener all gallery', error)
              this.loading = false;
          }
      )
  }

  ngOnDestroy(): void {
      this.albumsSubscription.unsubscribe();
  }
}
