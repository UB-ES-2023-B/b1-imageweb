import { Component,OnInit,  HostListener } from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'


@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  isMobile: boolean = false;

  constructor(private globalDataService:GlobalDataService) { }


  @HostListener('window:resize', ['$event'])
  onResize(event: any): void {
    this.checkScreenSize();
  }

  ngOnInit() {
    this.checkScreenSize();
    // this.globalDataService.username= '';
  }

  checkScreenSize(): void {
    this.isMobile = window.innerWidth <= 768;
  }


}
