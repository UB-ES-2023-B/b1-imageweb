import { Component, Input, Output, EventEmitter  } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent {
  @Input() modalTitle: string = 'Modal Title';
  @Input() ButtonText: string = 'abrir Modal';
  @Input() saveButtonText: string = 'Guardar';
  @Input() buttonColor:string='btn-primary';
  @Input() body: string = '';
  @Output() modalClosed = new EventEmitter<string>();



  constructor(  private modalService: NgbModal) {}

  openModal(content: any) {


    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.modalClosed.emit(result);

    }, (reason) => {
      this.modalClosed.emit(reason);

    });
  }



}
