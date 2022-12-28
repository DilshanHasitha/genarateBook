import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImageStoreTypeComponent } from '../list/image-store-type.component';
import { ImageStoreTypeDetailComponent } from '../detail/image-store-type-detail.component';
import { ImageStoreTypeUpdateComponent } from '../update/image-store-type-update.component';
import { ImageStoreTypeRoutingResolveService } from './image-store-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const imageStoreTypeRoute: Routes = [
  {
    path: '',
    component: ImageStoreTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImageStoreTypeDetailComponent,
    resolve: {
      imageStoreType: ImageStoreTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImageStoreTypeUpdateComponent,
    resolve: {
      imageStoreType: ImageStoreTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImageStoreTypeUpdateComponent,
    resolve: {
      imageStoreType: ImageStoreTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageStoreTypeRoute)],
  exports: [RouterModule],
})
export class ImageStoreTypeRoutingModule {}
