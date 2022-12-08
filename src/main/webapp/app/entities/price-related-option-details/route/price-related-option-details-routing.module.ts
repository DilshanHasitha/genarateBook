import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PriceRelatedOptionDetailsComponent } from '../list/price-related-option-details.component';
import { PriceRelatedOptionDetailsDetailComponent } from '../detail/price-related-option-details-detail.component';
import { PriceRelatedOptionDetailsUpdateComponent } from '../update/price-related-option-details-update.component';
import { PriceRelatedOptionDetailsRoutingResolveService } from './price-related-option-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const priceRelatedOptionDetailsRoute: Routes = [
  {
    path: '',
    component: PriceRelatedOptionDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PriceRelatedOptionDetailsDetailComponent,
    resolve: {
      priceRelatedOptionDetails: PriceRelatedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PriceRelatedOptionDetailsUpdateComponent,
    resolve: {
      priceRelatedOptionDetails: PriceRelatedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PriceRelatedOptionDetailsUpdateComponent,
    resolve: {
      priceRelatedOptionDetails: PriceRelatedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(priceRelatedOptionDetailsRoute)],
  exports: [RouterModule],
})
export class PriceRelatedOptionDetailsRoutingModule {}
