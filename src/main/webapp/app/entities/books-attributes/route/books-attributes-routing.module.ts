import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksAttributesComponent } from '../list/books-attributes.component';
import { BooksAttributesDetailComponent } from '../detail/books-attributes-detail.component';
import { BooksAttributesUpdateComponent } from '../update/books-attributes-update.component';
import { BooksAttributesRoutingResolveService } from './books-attributes-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksAttributesRoute: Routes = [
  {
    path: '',
    component: BooksAttributesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksAttributesDetailComponent,
    resolve: {
      booksAttributes: BooksAttributesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksAttributesUpdateComponent,
    resolve: {
      booksAttributes: BooksAttributesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksAttributesUpdateComponent,
    resolve: {
      booksAttributes: BooksAttributesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksAttributesRoute)],
  exports: [RouterModule],
})
export class BooksAttributesRoutingModule {}
