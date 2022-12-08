import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBooksRelatedOption } from '../books-related-option.model';

@Component({
  selector: 'jhi-books-related-option-detail',
  templateUrl: './books-related-option-detail.component.html',
})
export class BooksRelatedOptionDetailComponent implements OnInit {
  booksRelatedOption: IBooksRelatedOption | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksRelatedOption }) => {
      this.booksRelatedOption = booksRelatedOption;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
