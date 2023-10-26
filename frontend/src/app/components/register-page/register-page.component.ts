import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
  showPasswordError: boolean = false;
  showConPasswordError: boolean = false;
  registrationForm: FormGroup;
  showEmailError: boolean = false;
  usernameError: boolean = false;
  emailExisting: boolean = false;


  constructor(private globalDataService: GlobalDataService,
              private http: HttpClient,
              private router: Router,
              private authService: AuthenticationService,
              private fb: FormBuilder) {
              this.registrationForm = this.fb.group({
                // Other form controls
                email: ['', [Validators.required, Validators.pattern(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)]],
              });
  }

  ngOnInit(): void {
  }

  onPasswordChange() {
    this.showPasswordError = false;  // Reset error message on password change
  }

  onPasswordBlur() {
    this.showPasswordError = this.password.length < 6;
  }

  onConPasswordChange() {
    this.showConPasswordError = false;  // Reset error message on password change
  }

  onConPasswordBlur() {
    if(this.password != this.conPassword){
      this.showConPasswordError = true;
    }
  }

  onSubmit(): void {

    this.usernameError= false;
    this.emailExisting= false;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.showEmailError = !emailRegex.test(this.email);
    console.log(this.showEmailError);
    if(this.password == this.conPassword && this.password.length >= 6 && !this.showEmailError){
      console.log('Submitted!', this.email, this.password);
      this.authService.register(this.username, this.email, this.password)
        .subscribe(
          (response) => {
            if (response.status === 200) {
              console.log('Response:', response);
              this.globalDataService.setUsername(this.username);
              this.globalDataService.setToken(response.body.token);
              this.globalDataService.setEmail(this.email);
              this.router.navigate(['/home']);
            }
          },
          (error) => {

            console.error('Error during registration:', error);
            if (error.error.message.includes('Username')){
              this.usernameError = true;
            } else if (error.error.message.includes('Email')){
              this.emailExisting = true;
            }
          }
        );
    }
    else {
      console.error('Passwords do not match');
    }
  }
}
