import { Component ,ViewChild, ElementRef } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { AlbumsService } from 'src/app/services/albums.service';


class ImageSnippet {
  pending: boolean = false;
  status: 'init' | 'ok' | 'fail' = 'init';

  constructor(public src: string, public file: File) {}
}
@Component({
  selector: 'app-upload-photos',
  templateUrl: './upload-photos.component.html',
  styleUrls: ['./upload-photos.component.css']
})
export class UploadPhotosComponent {

  selectedFiles: ImageSnippet[] = [];
  imageChangedEvent: any;
  idAlbumActual:number= 1;
  file:File[]=[];
  loading=false;

  constructor(
    private toastr: ToastrService,
    private albumsService: AlbumsService,
    private globalDataService: GlobalDataService
  ) {}


  private onSuccess(response: any) {

    let id = 1563; //
    let name = 'Name photo'; //
    let description = 'Description photo';
    //Actuliza el albumsService la cantidad de imagenes. y la foto de perfil
    // this.albumsService.addImage(
    //   this.selectedFiles[index].src,
    //   id,
    //   name,
    //   description
    // );
    this.loading=false;
    this.toastr.success('Fotos cargadas satisfactoriamente');


  }



  private onError(message: string) {
    if (message === '') message = 'Error al subir fotos';
    this.selectedFiles.forEach((file) => {
      file.pending = false;
      file.status = 'fail';
      file.src = '';
    });
    this.toastr.error(message, 'Error');
    this.loading=false;
  }

  processFile(imageInput: any) {
    this.loading=true
    const files: File[] = imageInput.files;
    for (let i = 0; i < files.length; i++) {
      const file: File = files[i];
      const reader = new FileReader();
      reader.addEventListener('load', (event: any) => {
        const imageSnippet = new ImageSnippet(event.target.result, file);
        imageSnippet.pending = false;
        this.selectedFiles.push(imageSnippet);
        this.file.push(file);
        if( i==files.length-1){
          this.albumsService.addPhotosToAlbum(this.idAlbumActual,this.file)
          .subscribe(
              (response) => {
                console.log('mira response:',response)
                this.onSuccess(response)
          },
          (error) => {
          let message=""
          if (error.status==400){
            message="Ha superado el tamaño de 2 MB"
          }
          this.onError(message);
          }
         );
        }

      });

      reader.readAsDataURL(file);
    }
    console.log('esto es select NO SE LLENO?', this.selectedFiles)
    console.log('Este es el que esta pasandoooooo', this.file)


  }

}
