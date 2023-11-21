import { Component ,ViewChild, ElementRef } from '@angular/core';
import { GalleryService } from 'src/app/services/gallery.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { GlobalDataService } from 'src/app/services/global-data.service';


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

  constructor(
    private toastr: ToastrService,
    private galleryService: GalleryService,
    private globalDataService: GlobalDataService
  ) {}


  private onSuccess(response: any, index: number) {
    this.selectedFiles[index].pending = false;
    this.selectedFiles[index].status = 'ok';
    let id = 1563; // Set your ID logic here
    let name = 'Name photo'; // Set your name logic here
    let description = 'Description photo'; // Set your description logic here
    this.galleryService.addImage(
      this.selectedFiles[index].src,
      id,
      name,
      description
    );
    if (index === this.selectedFiles.length - 1) {
      this.toastr.success('Imagen cargada satisfactoriamente');
    }
  }



  private onError(message: string) {
    if (message === '') message = 'Error al subir la imagen';
    this.selectedFiles.forEach((file) => {
      file.pending = false;
      file.status = 'fail';
      file.src = '';
    });
    this.toastr.error(message, 'Error');
  }

  processFile(imageInput: any) {
    const files: File[] = imageInput.files;
    console.log('esto es select', this.selectedFiles)
    console.log('SI QUE ENTRAAAAAAAAAAAAAA')
    for (let i = 0; i < files.length; i++) {
      const file: File = files[i];
      const reader = new FileReader();

      reader.addEventListener('load', (event: any) => {
        const imageSnippet = new ImageSnippet(event.target.result, file);
        imageSnippet.pending = true;

        if (this.globalDataService.getGalleryId() === '') {
          console.log('No hay id gallery!!');
          return;
        }
        this.selectedFiles.push(imageSnippet);
        console.log('MIRAAAAAAAAA',imageSnippet.file)

      });

      reader.readAsDataURL(file);
    }
    console.log('esto es select NO SE LLENO?', this.selectedFiles)

  }

}
