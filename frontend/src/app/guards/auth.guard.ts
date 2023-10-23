import { CanActivateFn, Router } from '@angular/router';
import { GlobalDataService } from '../services/global-data.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const globalData: GlobalDataService = inject(GlobalDataService);
  const router: Router = inject(Router);
  if (globalData.getToken()!='' ){
    return true;
  }else{
    router.navigate(['/login'])
    return false;
  }
};
