import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBooksAttributes } from '../books-attributes.model';

@Component({
  selector: 'jhi-books-attributes-detail',
  templateUrl: './books-attributes-detail.component.html',
})
export class BooksAttributesDetailComponent implements OnInit {
  booksAttributes: IBooksAttributes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksAttributes }) => {
      this.booksAttributes = booksAttributes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
