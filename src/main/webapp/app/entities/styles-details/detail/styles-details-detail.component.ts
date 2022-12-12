import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStylesDetails } from '../styles-details.model';

@Component({
  selector: 'jhi-styles-details-detail',
  templateUrl: './styles-details-detail.component.html',
})
export class StylesDetailsDetailComponent implements OnInit {
  stylesDetails: IStylesDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stylesDetails }) => {
      this.stylesDetails = stylesDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
