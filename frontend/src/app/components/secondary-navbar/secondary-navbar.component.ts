import { Component } from '@angular/core';

@Component({
  selector: 'app-secondary-navbar',
  templateUrl: './secondary-navbar.component.html',
  styleUrls: ['./secondary-navbar.component.css']
})
export class SecondaryNavbarComponent {
  items: string[] = ['Home', 'About', 'Services', 'Contact'];
  constructor() { }

  ngOnInit(): void {
  }
}
