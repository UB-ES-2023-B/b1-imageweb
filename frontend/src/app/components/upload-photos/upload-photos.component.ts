import { Component ,Input, ElementRef } from '@angular/core';
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
  idAlbumActual:number= 13;
  file:File[]=[];
  loading=false;
  @Input() idAlbum!: number


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
    this.selectedFiles = [];


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
    if(!this.idAlbum){
        console.log('Hay un error con el id del albúm')
    }
    const files: File[] = imageInput.files;
    for (let i = 0; i < files.length; i++) {
      // Limpiar estados antes de procesar nuevos archivos
      this.selectedFiles = [];
      this.file = [];
      const file: File = files[i];
      const reader = new FileReader();
      reader.addEventListener('load', (event: any) => {
        const imageSnippet = new ImageSnippet(event.target.result, file);
        imageSnippet.pending = false;
        this.selectedFiles.push(imageSnippet);
        this.file.push(file);
        if( i==files.length-1){
          this.albumsService.addPhotosToAlbum(this.idAlbum,this.file)
          .subscribe(
              (response: any) => {
                console.log('mira response del upload:', response);
                if (response.body) {
                  if (response.body.length>0) {
                    console.log(response.body[0]);
                    this.albumsService.modifylen(this.idAlbum, response.body.length-1);
                    if(response.body.length>1){
                      var src= `data:image/${response.body[1].photoExtension};base64,${response.body[1].data}`
                      this.albumsService.modifyCoverPhoto(this.idAlbum, src)

                    }
                  }

                }
                imageInput.value = null;
                this.onSuccess(response);


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
      // imageInput.value = null;

      reader.readAsDataURL(file);
    }



  }

}
