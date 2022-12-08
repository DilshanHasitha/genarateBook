import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AvatarCharactorComponent } from '../list/avatar-charactor.component';
import { AvatarCharactorDetailComponent } from '../detail/avatar-charactor-detail.component';
import { AvatarCharactorUpdateComponent } from '../update/avatar-charactor-update.component';
import { AvatarCharactorRoutingResolveService } from './avatar-charactor-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const avatarCharactorRoute: Routes = [
  {
    path: '',
    component: AvatarCharactorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AvatarCharactorDetailComponent,
    resolve: {
      avatarCharactor: AvatarCharactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AvatarCharactorUpdateComponent,
    resolve: {
      avatarCharactor: AvatarCharactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AvatarCharactorUpdateComponent,
    resolve: {
      avatarCharactor: AvatarCharactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(avatarCharactorRoute)],
  exports: [RouterModule],
})
export class AvatarCharactorRoutingModule {}
