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
  showUserError: boolean = false;
  showEmailError: boolean = false;
  usernameError: boolean = false;
  emailExisting: boolean = false;
  showPassword: boolean = false;
  showConPassword: boolean = false;
  loginError: boolean = false;
  registrationForm: FormGroup;


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

  onEmailChange() {
    this.showEmailError = false;  // Reset error message on password change
  }

  onEmailBlur() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.showEmailError = !emailRegex.test(this.email);
    console.log(this.showEmailError);
  }

  onUserChange() {
    this.showUserError = false;  // Reset error message on password change
  }

  onUserBlur() {
    if(this.username == ""){
      this.showUserError = true;
    }
  }

  onPasswordChange() {
    this.showPasswordError = false;
  }

  onPasswordBlur() {
    const passRegex = /^(?=.*[!@#$%^&_*,.])((?=.*[A-Z])(?=.*[a-z])(?=.*\d)).{6,}$/;
    this.showPasswordError = !passRegex.test(this.password);
    if(this.password != this.conPassword && this.conPassword.length > 0){
      this.showConPasswordError = true;
    }else{
      this.showConPasswordError = false;
    }
  }

  onConPasswordChange() {
    this.showConPasswordError = false;  // Reset error message on password change
  }

  onConPasswordBlur() {
    if(this.password != this.conPassword){
      this.showConPasswordError = true;
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConPasswordVisibility() {
    this.showConPassword = !this.showConPassword;
  }

  onSubmit(): void {

    this.usernameError= false;
    this.emailExisting= false;
    this.loginError= false;

    if(this.password == this.conPassword && this.password.length >= 6 && !this.showEmailError && !this.showUserError && !this.showPasswordError){
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
            this.loginError = true;
            console.log(this.loginError);
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
