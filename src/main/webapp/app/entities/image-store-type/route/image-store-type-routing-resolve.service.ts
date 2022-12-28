import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageStoreType } from '../image-store-type.model';
import { ImageStoreTypeService } from '../service/image-store-type.service';

@Injectable({ providedIn: 'root' })
export class ImageStoreTypeRoutingResolveService implements Resolve<IImageStoreType | null> {
  constructor(protected service: ImageStoreTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImageStoreType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imageStoreType: HttpResponse<IImageStoreType>) => {
          if (imageStoreType.body) {
            return of(imageStoreType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
