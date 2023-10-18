import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpResponse  } from '@angular/common/http';
import { GlobalDataService } from '../../services/global-data.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../services/authentication.service';
@Component({
  selector: 'app-login-form',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginFormComponent implements OnInit {
  username: string = '';
  password: string = '';
  loginError: boolean = false;

  constructor(private globalDataService: GlobalDataService,
              private http: HttpClient,
              private router: Router,
              private authService: AuthenticationService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.loginError= false;
      this.authService.login(this.username, this.password)
        .subscribe(
          (response) => {
            if (response.status === 200) {
              console.log('Response:', response);
              this.globalDataService.setToken(response.body.token);
              this.globalDataService.setUsername(this.username);
              this.router.navigate(['/home']);
            }
          },
          (error) => {
            console.error('Error during login:', error);
            this.loginError = true;
          }
        );
    }
}
