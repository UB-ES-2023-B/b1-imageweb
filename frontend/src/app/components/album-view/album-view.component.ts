import { Component, Input } from '@angular/core';
import { AlbumsService } from '../../services/albums.service';
import { UserService } from '../../services/user.service';
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

  constructor(private albumsService:AlbumsService, private userService:UserService, private router: Router, private globalDataService:GlobalDataService, private _lightbox: Lightbox, private toastr: ToastrService, private route: ActivatedRoute){
  }

  goGallery(){
    this.globalDataService.setActiveItem('albumes');
    if(this.original_username == this.visitor_username){
      this.router.navigate(['/profile/']);
    }
    else{
      this.router.navigate(['/user-profile/'+ this.original_userId]);
    }
  }

  goEditMode(){
    console.log(this.visitor_username);
    this.router.navigate(["/profile/album/" + this.albumId + "/editMode"]);
  }

  getAlbum():void{
    this.images=[]
    this.albumsService.getAlbumById(this.albumId).subscribe(
      (response)=>{

        if (response.body && Array.isArray(response.body)) {
          this.albumName = response.body[0].albums[0].albumName;
          this.albumDescription = response.body[0].albums[0].description;
          this.albumLenght = response.body.length-1+ " fotos";

          if(response.body.length == 1){
            this.loading=false;
          }
          response.body.forEach((element: any) => {

            if (element.data) {
              if(element.photoName != "defaultImage"){
                if (response.body.indexOf(element) == 1){
                  this.userService.getUserAlbumOwner(element.gallery.galleryrId).subscribe(
                    (response) =>{
                      this.original_username = response.body.username;
                      this.original_userId = response.body.userId;
                      this.loading=false;
                    }
                  )
                  
                  this.coverImage = {
                    "src":`data:image/${element.photoExtensio};base64,${element.data}`,
                    "id": element.photoId, "name":element.photoName, "description": element.photoDescription
                  }
                }
                this.images.unshift({
                  "src":`data:image/${element.photoExtensio};base64,${element.data}`,
                  "id": element.photoId, "name":element.photoName, "description": element.photoDescription
                });

              }
            }
          });
        }
        this.albumsService.setImagesToAlbum(this.images);
        this.loading=false;

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

