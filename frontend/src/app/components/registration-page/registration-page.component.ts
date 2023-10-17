import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationFormComponent implements OnInit {
  name: string = '';
  email: string = '';
  password: string = '';

  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    // Handle form submission logic here
    console.log('Registration form submitted!');
    console.log(`Name: ${this.name}`);
    console.log(`Email: ${this.email}`);
    console.log(`Password: ${this.password}`);
  }
}
