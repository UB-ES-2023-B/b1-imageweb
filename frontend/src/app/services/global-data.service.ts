import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalDataService {
  public username: string='';
  public email: string='';
  constructor() { }
}
