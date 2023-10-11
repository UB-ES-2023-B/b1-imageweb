import { Component, OnInit } from '@angular/core';

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

  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    // Handle form submission logic here
    if(this.password == this.conPassword){
      console.log('Submitted!', this.username, this.email);
    }
    else{
      console.error('Passwords do not match');
    }
  }
}
