import { Component  } from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  username: string = '';
  private usernameSubscription: Subscription = new Subscription();


  constructor(private globalDataService:GlobalDataService) { }

  ngOnInit(): void {
    this.usernameSubscription = this.globalDataService.username$.subscribe(username => {
      this.username = username;
    });
  }
  ngOnDestroy() {
    this.usernameSubscription.unsubscribe();
  }

}
