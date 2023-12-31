import { Component, ViewChild, ElementRef, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { AlbumsService } from '../../services/albums.service';
import { GlobalDataService } from 'src/app/services/global-data.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

class ImageSnippet {
  pending: boolean = false;
  status: 'init' | 'ok' | 'fail' = 'init';

  constructor(public src: string, public file: File) {
  }
}

@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.css']
})

export class AlbumsComponent {
  @Input() username: string = '';
  original_username: string = this.globalDataService.getUsername();


  modalTitle: string = 'Nuevo álbum'
  name: string = '';
  description: string = '';
  remainingWords: number = 25;
  loading: boolean = true;
  albums: any[] = [];
  defaultImage!: ImageSnippet;

  private albumsSubscription: Subscription = new Subscription();


  constructor(private modalService: NgbModal,
    private albumsService: AlbumsService,
    private toastr: ToastrService,
    private router: Router,
    private globalDataService: GlobalDataService) {
  }


  ngOnInit(): void {
    this.getFile();
    this.albumsSubscription = this.albumsService.getAlbumsObservable().subscribe((albums) => {
      this.albums = albums;
    });
    this.getAlbums();
  }

  getAlbums() {
    this.albums = []
    this.albumsService.getAlbumsForUser(this.username).subscribe(
      (response) => {
        if (response.body) {
          response.body.forEach((element: any) => {
            let src = `data:image/${element.coverPhoto.photoExtension};base64,${ element.coverPhoto.data}`;
            this.albums.unshift({
              "src": src,
              "id": element.albumId,
              "name": element.name,
              "description": element.description,
              "photoLength": element.length-1
            });
          });
        }
        this.albumsService.setAlbums(this.albums);
        this.loading = false;
      },
      (error) => {
        console.log('error al obtener all gallery', error)
        this.loading = false;
      }
    )
  }

  goEditAlbum() {
    this.router.navigate(['/profile/editAlbums']);

  }

  openModal(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      if (result == 'save') {
        this.save();
      } else {
        this.close();
      }

    }, (reason) => {
      this.close();

    });
  }

  getFile() {
    const route = "../../../assets/images/defaultImageAlbum.jpg";

    // Crear una función para cargar el contenido del archivo como Blob
    const loadFileAsBlob = (ruta: string): Promise<Blob> => {
      return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', ruta, true);
        xhr.responseType = 'blob';

        xhr.onload = () => {
          if (xhr.status === 200) {
            resolve(xhr.response);
          } else {
            reject(new Error('Error al cargar el archivo'));
          }
        };

        xhr.onerror = () => {
          reject(new Error('Error de red al cargar el archivo'));
        };

        xhr.send();
      });
    };

    // Cargar el contenido del archivo como Blob y luego usar FileReader
    loadFileAsBlob(route)
      .then((blob) => {
        const fileReader = new FileReader();
        fileReader.onload = (event: any) => {
          const content = event.target.result;
          const name = 'defaultImage';
          const file = new File([blob], name, { type: blob.type });
          this.defaultImage = new ImageSnippet(content, file);
        };

        fileReader.readAsDataURL(blob);
      })
      .catch(error => {
        console.error('Error al cargar el archivo:', error);
      });
  }

  save() {
    //Hacer llamada del back
    this.albumsService.createAlbum(this.name, this.description, this.defaultImage.file).subscribe(
      (response) => {
        if (response.body.length > 0) {
          var idAlbum = response.body[0].albums[0].albumId
        } else {
          console.log('error con el id del álbum')
        }
        this.albums.unshift({
          "src": '../../../assets/images/defaultImageAlbum.jpg',
          "id": idAlbum, "name": this.name, "description": this.description, "photoLength": 0
        });
        this.albumsService.setAlbums(this.albums);
        this.toastr.success("Álbum creado");
        this.name = '';
        this.description = '';
        this.loading = false;
      },
      (error) => {
        console.log('error al crear album', error)
        this.loading = false;
        this.toastr.error('Error al crear álbum, inténtalo de nuevo', 'Error');
      }
    )
  }

  close() {
    this.name = '';
    this.description = '';
    this.remainingWords = 25;
  }

  countWords() {
    const words = this.description.split(/\s+/).filter((word) => word.length > 0);
    const remainingWords = 25 - words.length;

    if (remainingWords >= 0) {
      this.remainingWords = remainingWords;
    } else {
      // Limitar el número de palabras en el textarea
      const limit = words.slice(0, 25).join(' ');
      this.description = limit;
      this.remainingWords = 0;
    }
  }

  ngOnDestroy(): void {
    this.albumsSubscription.unsubscribe();
  }

  onAlbumClick(albumId: number) {
    this.router.navigate(["/profile/album", albumId]);
  }
}
