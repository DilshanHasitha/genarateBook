import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksComponent } from '../list/books.component';
import { BooksDetailComponent } from '../detail/books-detail.component';
import { BooksUpdateComponent } from '../update/books-update.component';
import { BooksRoutingResolveService } from './books-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksRoute: Routes = [
  {
    path: '',
    component: BooksComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksDetailComponent,
    resolve: {
      books: BooksRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksUpdateComponent,
    resolve: {
      books: BooksRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksUpdateComponent,
    resolve: {
      books: BooksRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksRoute)],
  exports: [RouterModule],
})
export class BooksRoutingModule {}
