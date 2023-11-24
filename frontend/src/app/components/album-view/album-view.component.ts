import { Component } from '@angular/core';
import { AlbumsService } from '../../services/albums.service';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { Subscription } from 'rxjs';
import { Lightbox } from 'ngx-lightbox';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

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

  constructor(private albumsService:AlbumsService, private router: Router, private globalDataService:GlobalDataService, private _lightbox: Lightbox, private toastr: ToastrService){
  }

  goToAlbumsView(){
    this.router.navigate(['/profile/']);
  }

  getAlbum():void{
    this.images=[]
  }

  open(index: number): void {
    this._lightbox.open(this.images, index);
  }

  close(): void {
    this._lightbox.close();
  }

  ngOnInit(): void {
    const urlTree = this.router.parseUrl(this.router.url);
    this.albumId = urlTree.queryParams['id'];
  }

  ngOnDestroy(): void {
    this.imagesSubscription.unsubscribe();
  }


}

