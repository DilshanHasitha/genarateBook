import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksPageComponent } from '../list/books-page.component';
import { BooksPageDetailComponent } from '../detail/books-page-detail.component';
import { BooksPageUpdateComponent } from '../update/books-page-update.component';
import { BooksPageRoutingResolveService } from './books-page-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksPageRoute: Routes = [
  {
    path: '',
    component: BooksPageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksPageDetailComponent,
    resolve: {
      booksPage: BooksPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksPageUpdateComponent,
    resolve: {
      booksPage: BooksPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksPageUpdateComponent,
    resolve: {
      booksPage: BooksPageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksPageRoute)],
  exports: [RouterModule],
})
export class BooksPageRoutingModule {}
