import {Component, ViewChild, ElementRef} from '@angular/core';
import {GalleryService} from 'src/app/services/gallery.service';
import {ToastrService} from 'ngx-toastr';
import {HttpErrorResponse} from '@angular/common/http';
import {GlobalDataService} from 'src/app/services/global-data.service';
import { Router } from "@angular/router";

class ImageSnippet {
    pending: boolean = false;
    status: string = 'init';

    constructor(public src: string, public file: File) {
    }
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
                private gallleryService: GalleryService,
                private globalDataService: GlobalDataService,
                private router: Router) {

    }

    private onSuccess(response: any) {
        this.selectedFile.pending = false;
        this.selectedFile.status = 'ok';


        //falta enviarle, el id , el nombre y la descripcion.
        let id = response.body.photoId;
        let name = response.body.photoName;
        let description = response.body.photoDescription;

        this.router.navigate(["/profile"])
        this.gallleryService.addImage(this.selectedFile.src, id, name, description);
        this.toastr.success('Imagen cargada satisfactoriamente');

    }

    private onError(message: string) {
        if (message == "") message = "Error al subir la imagen"
        this.selectedFile.pending = false;
        this.selectedFile.status = 'fail';
        this.selectedFile.src = '';
        this.toastr.error(message, 'Error');
    }

    processFile(imageInput: any) {
        const file: File = imageInput.files[0];
        const reader = new FileReader();

        reader.addEventListener('load', (event: any) => {
            this.selectedFile = new ImageSnippet(event.target.result, file);
            this.selectedFile.pending = true;
            if (this.globalDataService.getGalleryId() == '') {
                this.selectedFile.pending = false;
                this.toastr.error('Recarga galería', 'No ID');
                console.log('No hay id gallery!!')
                return
            } else {
                console.log('esto es lo que es file:::::::: Galeria', this.selectedFile.file)
                this.gallleryService.uploadImage(this.globalDataService.getGalleryId(), this.selectedFile.file)
                    .subscribe(
                        (response) => {
                            this.onSuccess(response);
                        },
                        (error) => {
                            let message = ""
                            if (error.status == 400) {
                                message = "Ha superado el tamaño de 2 MB"
                            }
                            this.onError(message);
                        }
                    );
            }
        });
        reader.readAsDataURL(file);
    }
}
