import { Component  } from '@angular/core';
import {GlobalDataService} from '../../services/global-data.service'
import { Subscription } from 'rxjs';
import { Router } from '@angular/router'; // Importa el módulo Router

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  username: string = '';
  private usernameSubscription: Subscription = new Subscription();


  constructor(private globalDataService:GlobalDataService, private router: Router) { }

  ngOnInit(): void {
    this.usernameSubscription = this.globalDataService.username$.subscribe(username => {
      this.username = username;
    });
  }
  ngOnDestroy() {
    this.usernameSubscription.unsubscribe();
  }

  logout() {
    this.globalDataService.clearSession();
    this.router.navigate(['/#']); // Redirige al usuario a la página de inicio

  }

  uploadImage(): void {

      console.log('Cargando imagen en proceso');

  }

}
