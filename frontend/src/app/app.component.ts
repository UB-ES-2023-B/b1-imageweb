import {Component, OnInit} from '@angular/core';
import {DataService} from "./data.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'frontend';
  name: string = 'Hola';

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getUser().subscribe((response) => {
      console.log(response)
      this.name = response[0].userName;
    })
  }
}
