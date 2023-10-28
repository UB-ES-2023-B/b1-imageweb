import { Component } from '@angular/core';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent {
  images:any[]=[
    { "image": "https://acortar.link/ulBlOB" },
    { "image": "https://acortar.link/bWqxtC" },
    { "image": "https://acortar.link/wcLNmV" },
    { "image": "https://acortar.link/TjJO34" },
    { "image": "https://acortar.link/dNN01E" },
    { "image": "https://acortar.link/wQFi3z" },
  ]
}
