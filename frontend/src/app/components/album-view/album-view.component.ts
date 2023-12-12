import { Component, Input } from '@angular/core';
import { AlbumsService } from '../../services/albums.service';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { Subscription } from 'rxjs';
import { Lightbox } from 'ngx-lightbox';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-album-view',
  templateUrl: './album-view.component.html',
  styleUrls: ['./album-view.component.css']
})

export class AlbumViewComponent {

  loading:boolean=true;
  images:any[]=[];
  coverImage:any = null;
  showUploadHint: boolean = true;
  albumId: any;
  albumName: any;
  albumDescription: any;
  albumLenght: any;

  visitor_username: string = this.globalDataService.getUsername();
  original_username: any;
  original_userId: any;

  private imagesSubscription: Subscription = new Subscription();

  constructor(private albumsService:AlbumsService, private router: Router, private globalDataService:GlobalDataService, private _lightbox: Lightbox, private toastr: ToastrService, private route: ActivatedRoute){
  }

  goGallery(){
    this.globalDataService.setActiveItem('albumes');
    console.log(this.visitor_username);
    console.log(this.original_username);

    if(this.original_username == this.visitor_username){
      this.router.navigate(['/profile/']);
    }
    else{
      this.router.navigate(['/user-profile/'+ this.original_userId]);
    }
  }

  goEditMode(){
    this.router.navigate(["/profile/album/" + this.albumId + "/editMode"]);
  }

  getAlbum():void{
    var hasCoverPhoto = false;
    this.images=[]
    this.albumsService.getAlbumById(this.albumId).subscribe(
      (response)=>{
        if (response.body && Array.isArray(response.body)) {
          response.body[0].albums.forEach((element: any) => {
            if(element.albumId == this.albumId){
              this.albumName = element.albumName;
              this.albumDescription = element.description;
            }
            
          });
          this.albumLenght = response.body.length-1+ " fotos";

          response.body.forEach((element: any) => {

            if (element.data) {
              if(element.photoName != "defaultImage"){
                if (!hasCoverPhoto){
                  this.coverImage = {
                    "src":`data:image/${element.photoExtensio};base64,${element.data}`,
                    "id": element.photoId, "name":element.photoName, "description": element.photoDescription
                  }
                  hasCoverPhoto = true;
                }
                this.images.unshift({
                  "src":`data:image/${element.photoExtensio};base64,${element.data}`,
                  "id": element.photoId, "name":element.photoName, "description": element.photoDescription
                });

              }else{
                this.albumsService.getUserAlbumOwner(this.albumId).subscribe(
                  (response) =>{
                    this.original_username = response.body.username;
                    this.original_userId = response.body.userId;
                    this.loading=false;
                  }
                )
              }
            }
          });
        }
        this.albumsService.setImagesToAlbum(this.images);

      },(error)=>{
        console.log('error al obtener album', error)
      }

    );

  }

  open(index: number): void {
    this._lightbox.open(this.images, index);
  }

  close(): void {
    this._lightbox.close();
  }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.albumId = params['id'];
    });

    this.imagesSubscription = this.albumsService.getImagesObservable().subscribe((images) => {
      this.images = images;
    });

    this.getAlbum();
  }

  ngOnDestroy(): void {
    this.imagesSubscription.unsubscribe();
  }


}

