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

  @ViewChild('imageInput') imageInput!: ElementRef;


  selectedFile!: ImageSnippet;
  imageChangedEvent: any;



  constructor(private toastr: ToastrService,
    private gallleryService: GalleryService,private globalDataService:GlobalDataService) {

  }

  private onSuccess() {
    this.selectedFile.pending = false;
    this.selectedFile.status = 'ok';
    this.gallleryService.addImage(this.selectedFile.src)
    this.toastr.success('Imagen cargada satisfactoriamente');
    this.resetImageInput(); // Llamamos a la función para restablecer el campo de entrada de archivos



  }
  private resetImageInput() {
    if (this.imageInput && this.imageInput.nativeElement) {
      this.imageInput.nativeElement.value = ''; // Reiniciamos el valor del campo de entrada de archivos
      console.log('cambia algooooooooooooooooooooooooooo')
    }
  }
  private onError() {
    this.selectedFile.pending = false;
    this.selectedFile.status = 'fail';
    this.selectedFile.src = '';
    this.toastr.error('Error al subir la imagen');
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
            this.onSuccess();

        },
        (error) => {
            this.onError();
            console.error('Error en la subida de la imagen:', error);
      }

     );



    });

    reader.readAsDataURL(file);
  }


}
