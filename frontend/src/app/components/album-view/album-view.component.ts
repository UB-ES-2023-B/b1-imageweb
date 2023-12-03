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
  showUploadHint: boolean = true;
  albumId: any;

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
          response.body.forEach((element: any) => {
            if (element.data) {
              if(element.photoName != "defaultImage"){
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
      
    )
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

