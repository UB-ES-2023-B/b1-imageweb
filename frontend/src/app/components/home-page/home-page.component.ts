import { Component } from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  username: string = '';

  constructor(private globalDataService:GlobalDataService) { }

  ngOnInit(): void {
    this.username = this.globalDataService.getUsername();

  }




}
