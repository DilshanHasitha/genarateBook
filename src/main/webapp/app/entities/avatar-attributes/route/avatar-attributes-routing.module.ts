import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AvatarAttributesComponent } from '../list/avatar-attributes.component';
import { AvatarAttributesDetailComponent } from '../detail/avatar-attributes-detail.component';
import { AvatarAttributesUpdateComponent } from '../update/avatar-attributes-update.component';
import { AvatarAttributesRoutingResolveService } from './avatar-attributes-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const avatarAttributesRoute: Routes = [
  {
    path: '',
    component: AvatarAttributesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AvatarAttributesDetailComponent,
    resolve: {
      avatarAttributes: AvatarAttributesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AvatarAttributesUpdateComponent,
    resolve: {
      avatarAttributes: AvatarAttributesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AvatarAttributesUpdateComponent,
    resolve: {
      avatarAttributes: AvatarAttributesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(avatarAttributesRoute)],
  exports: [RouterModule],
})
export class AvatarAttributesRoutingModule {}
