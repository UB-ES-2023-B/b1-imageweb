import { Component ,ViewChild, ElementRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.css']
})
export class AlbumsComponent {
  modalTitle:string='Nuevo álbum'
  name:string='';
  description:string='';
  remainingWords: number = 25;

  constructor(  private modalService: NgbModal) {}

  openModal(content: any) {


    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      if(result=='save'){
        this.save();
      }else{
        this.close();
      }

    }, (reason) => {
      this.close();

    });
  }
  save(){
    //Hacer llamada del back
  }
  close(){
    this.name='';
    this.description='';
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

}
