import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OptionTypeComponent } from '../list/option-type.component';
import { OptionTypeDetailComponent } from '../detail/option-type-detail.component';
import { OptionTypeUpdateComponent } from '../update/option-type-update.component';
import { OptionTypeRoutingResolveService } from './option-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const optionTypeRoute: Routes = [
  {
    path: '',
    component: OptionTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OptionTypeDetailComponent,
    resolve: {
      optionType: OptionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OptionTypeUpdateComponent,
    resolve: {
      optionType: OptionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OptionTypeUpdateComponent,
    resolve: {
      optionType: OptionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(optionTypeRoute)],
  exports: [RouterModule],
})
export class OptionTypeRoutingModule {}
