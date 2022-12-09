import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SelectionsComponent } from '../list/selections.component';
import { SelectionsDetailComponent } from '../detail/selections-detail.component';
import { SelectionsUpdateComponent } from '../update/selections-update.component';
import { SelectionsRoutingResolveService } from './selections-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const selectionsRoute: Routes = [
  {
    path: '',
    component: SelectionsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SelectionsDetailComponent,
    resolve: {
      selections: SelectionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SelectionsUpdateComponent,
    resolve: {
      selections: SelectionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SelectionsUpdateComponent,
    resolve: {
      selections: SelectionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(selectionsRoute)],
  exports: [RouterModule],
})
export class SelectionsRoutingModule {}
