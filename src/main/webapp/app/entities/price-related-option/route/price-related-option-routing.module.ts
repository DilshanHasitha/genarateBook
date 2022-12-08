import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PriceRelatedOptionComponent } from '../list/price-related-option.component';
import { PriceRelatedOptionDetailComponent } from '../detail/price-related-option-detail.component';
import { PriceRelatedOptionUpdateComponent } from '../update/price-related-option-update.component';
import { PriceRelatedOptionRoutingResolveService } from './price-related-option-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const priceRelatedOptionRoute: Routes = [
  {
    path: '',
    component: PriceRelatedOptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PriceRelatedOptionDetailComponent,
    resolve: {
      priceRelatedOption: PriceRelatedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PriceRelatedOptionUpdateComponent,
    resolve: {
      priceRelatedOption: PriceRelatedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PriceRelatedOptionUpdateComponent,
    resolve: {
      priceRelatedOption: PriceRelatedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(priceRelatedOptionRoute)],
  exports: [RouterModule],
})
export class PriceRelatedOptionRoutingModule {}
