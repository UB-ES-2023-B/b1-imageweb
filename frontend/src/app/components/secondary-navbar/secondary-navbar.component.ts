import { Component , Input, Output, EventEmitter  } from '@angular/core';
import { GlobalDataService } from 'src/app/services/global-data.service';
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
    { label: 'Álbumes', id: 'albumes' },
   ]

  constructor(private globalDataService:GlobalDataService) { }


  isActive(item: string): boolean {
    return this.activeItem === item;
  }

  setActive(item: string): void {
    this.activeItem = item;
    this.globalDataService.setActiveItem(item);
    this.itemClicked.emit(item);
  }



  ngOnInit(): void {
  }


}
