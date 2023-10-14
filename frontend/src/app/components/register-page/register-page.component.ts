import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GlobalDataService } from '../../services/global-data.service';
import { Router } from '@angular/router';

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
              private router: Router) {  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    // Handle form submission logic here
    if(this.password == this.conPassword){
    this.globalDataService.username = this.username;
      this.globalDataService.email = this.email;
      const formData = {
        username: this.username,
        email: this.email,
        password: this.password
      };
      this.http.post('/api/register', formData).subscribe(
        (response) => {
          console.log('Registration successful:', response);
          // Optionally, you can handle success response here
          this.router.navigate(['/home']);
        },
        (error) => {
          console.error('Error during registration:', error);
          // Optionally, you can handle error response here
        }
      );
      console.log('Submitted!', this.username, this.email);
    }
    else{
      console.error('Passwords do not match');
    }
  }
}
