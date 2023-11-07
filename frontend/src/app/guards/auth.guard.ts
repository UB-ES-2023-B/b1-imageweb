import { CanActivateFn, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { GlobalDataService } from '../services/global-data.service';
import { inject } from '@angular/core';

export const AuthGuard : CanActivateFn = (route:ActivatedRouteSnapshot, state:RouterStateSnapshot) => {
  const globalData: GlobalDataService = inject(GlobalDataService);
  const router: Router = inject(Router);
  if (globalData.getToken()!='' ){
    return true;
  }else{
    router.navigate(['/login'])
    return false;
  }
};
