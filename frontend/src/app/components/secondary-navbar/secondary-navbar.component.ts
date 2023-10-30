import { Component , Input, Output, EventEmitter  } from '@angular/core';

@Component({
  selector: 'app-secondary-navbar',
  templateUrl: './secondary-navbar.component.html',
  styleUrls: ['./secondary-navbar.component.css']
})
export class SecondaryNavbarComponent {

  @Input() activeItem: string ='';
  @Output() itemClicked = new EventEmitter<string>();

  navItems = [
    { label: 'Información', id: 'info' },
    { label: 'Galería', id: 'gallery' },
   ]

  constructor() { }


  isActive(item: string): boolean {
    return this.activeItem === item;
  }

  setActive(item: string): void {
    this.activeItem = item;
    this.itemClicked.emit(item);
  }



  ngOnInit(): void {
  }


}
