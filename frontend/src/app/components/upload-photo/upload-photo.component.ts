import { Component,  ViewChild, ElementRef  } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { GlobalDataService } from 'src/app/services/global-data.service';

class ImageSnippet {
  pending: boolean = false;
  status: string = 'init';

  constructor(public src: string, public file: File) {}
}

@Component({
  selector: 'app-upload-photo',
  templateUrl: './upload-photo.component.html',
  styleUrls: ['./upload-photo.component.css']
})
export class UploadPhotoComponent {

  selectedFile!: ImageSnippet;
  imageChangedEvent: any;

  constructor(private toastr: ToastrService,
    private gallleryService: GalleryService,private globalDataService:GlobalDataService) {

  }

  private onSuccess(response: any) {
    this.selectedFile.pending = false;
    this.selectedFile.status = 'ok';
    //falta enviarle, el id , el nombre y la descripcion.
    let id=1563;
    let name='Name photo';
    let description='Description photo';
    this.gallleryService.addImage(this.selectedFile.src,id,name,description)
    this.toastr.success('Imagen cargada satisfactoriamente');

  }

  private onError(message:string) {
    if(message=="") message="Error al subir la imagen"
    this.selectedFile.pending = false;
    this.selectedFile.status = 'fail';
    this.selectedFile.src = '';
    this.toastr.error(message,'Error');
  }

  processFile(imageInput: any) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {
      this.selectedFile = new ImageSnippet(event.target.result, file);
      this.selectedFile.pending = true;
      if(this.globalDataService.getGalleryId()==''){
        console.log('No hay id gallery!!')
        return
      }
      this.gallleryService.uploadImage(this.globalDataService.getGalleryId(),this.selectedFile.file)
      .subscribe(
        (response) => {
          console.log(response)
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
    });
    reader.readAsDataURL(file);
  }
}
