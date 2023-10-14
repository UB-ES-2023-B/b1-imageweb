import { Component } from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  username: string = 'Daniela';

  constructor(private globalDataService:GlobalDataService) { }

  ngOnInit(): void {
    console.log('username!!!',this.globalDataService.username);
  }




}
