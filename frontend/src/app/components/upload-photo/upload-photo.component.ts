import { Component } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

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
    private gallleryService: GalleryService) {

  }

  private onSuccess() {
    this.selectedFile.pending = false;
    this.selectedFile.status = 'ok';

    this.toastr.success('Imagen cargada satisfactoriamente');
    console.log('onSuccess() called');


  }

  private onError() {
    this.selectedFile.pending = false;
    this.selectedFile.status = 'fail';
    this.selectedFile.src = '';
  }

  processFile(imageInput: any) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {

      this.selectedFile = new ImageSnippet(event.target.result, file);

      this.selectedFile.pending = true;
      // this.imageService.uploadImage(this.selectedFile.file).subscribe(
      //   (res) => {
      //     this.onSuccess();
      //   },
      //   (err) => {
      //     this.onError();
      //   })
      this.onSuccess();
      console.log('falta llamar el service')
    });

    reader.readAsDataURL(file);
  }


}
