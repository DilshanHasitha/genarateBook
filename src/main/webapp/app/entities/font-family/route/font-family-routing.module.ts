import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FontFamilyComponent } from '../list/font-family.component';
import { FontFamilyDetailComponent } from '../detail/font-family-detail.component';
import { FontFamilyUpdateComponent } from '../update/font-family-update.component';
import { FontFamilyRoutingResolveService } from './font-family-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fontFamilyRoute: Routes = [
  {
    path: '',
    component: FontFamilyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FontFamilyDetailComponent,
    resolve: {
      fontFamily: FontFamilyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FontFamilyUpdateComponent,
    resolve: {
      fontFamily: FontFamilyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FontFamilyUpdateComponent,
    resolve: {
      fontFamily: FontFamilyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fontFamilyRoute)],
  exports: [RouterModule],
})
export class FontFamilyRoutingModule {}
