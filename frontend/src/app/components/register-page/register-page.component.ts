import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpResponse  } from '@angular/common/http';
import { GlobalDataService } from '../../services/global-data.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterFormComponent implements OnInit {
  username: string = '';
  password: string = '';
  email: string = '';
  conPassword: string = '';


  constructor(private globalDataService: GlobalDataService,
              private http: HttpClient,
              private router: Router,
              private authService: AuthenticationService) {  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if(this.password == this.conPassword){
      console.log('Submitted!', this.email, this.password);
      this.authService.register(this.username, this.email, this.password)
        .subscribe(
          (response) => {
            if (response.status === 200) {
              console.log('Response:', response);
              this.globalDataService.setUsername(this.username);
              this.globalDataService.email = this.email;
              this.router.navigate(['/home']);
            }
          },
          (error) => {
            console.error('Error during registration:', error);
          }
        );
    } else {
      console.error('Passwords do not match');
    }
  }
}
