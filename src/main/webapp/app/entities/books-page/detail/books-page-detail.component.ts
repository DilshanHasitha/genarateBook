import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBooksPage } from '../books-page.model';

@Component({
  selector: 'jhi-books-page-detail',
  templateUrl: './books-page-detail.component.html',
})
export class BooksPageDetailComponent implements OnInit {
  booksPage: IBooksPage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksPage }) => {
      this.booksPage = booksPage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
