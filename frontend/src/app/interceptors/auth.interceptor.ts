import { Injectable } from '@angular/core';
import { inject } from '@angular/core';

import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { GlobalDataService } from '../services/global-data.service';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let cloneRequest = request;
    const globalData: GlobalDataService = inject(GlobalDataService);
    if(globalData.getToken()!=''){
      cloneRequest= request.clone({
        setHeaders:{
          'Authorization':`Bearer ${globalData.getToken()}`
        }
      })
    }
    return next.handle(cloneRequest);
  }
}
