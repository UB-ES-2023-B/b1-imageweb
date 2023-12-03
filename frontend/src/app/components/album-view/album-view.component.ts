import { Component } from '@angular/core';
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

  private imagesSubscription: Subscription = new Subscription();

  constructor(private albumsService:AlbumsService, private router: Router, private globalDataService:GlobalDataService, private _lightbox: Lightbox, private toastr: ToastrService, private route: ActivatedRoute){
  }

  goGallery(){
    this.globalDataService.setActiveItem('albumes');
    this.router.navigate(['/profile/']);
  }

  getAlbum():void{
    this.images=[]
    this.albumsService.getAlbumById(this.albumId).subscribe(
      (response)=>{

        if (response.body && Array.isArray(response.body)) {
          let len = response.body.length;
          response.body.forEach((element: any) => {
            if (element.data) {
              if(element.photoName != "defaultImage"){
                if (response.body.indexOf(element) == 1){
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
        this.albumsService.getAlbumById(this.albumId).subscribe(
          (response)=>{
            if (response.body && Array.isArray(response.body)) {
              this.albumName = response.body[0].album.albumName;
              this.albumDescription = response.body[0].album.description;
              this.albumLenght = response.body.length-1+ " fotos";
            }
            this.loading=false;
          }
          
        );
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
    // Use ActivatedRoute to get the id from the URL path
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

