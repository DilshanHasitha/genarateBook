import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPriceRelatedOption } from '../price-related-option.model';

@Component({
  selector: 'jhi-price-related-option-detail',
  templateUrl: './price-related-option-detail.component.html',
})
export class PriceRelatedOptionDetailComponent implements OnInit {
  priceRelatedOption: IPriceRelatedOption | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceRelatedOption }) => {
      this.priceRelatedOption = priceRelatedOption;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
