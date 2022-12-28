import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFontFamily } from '../font-family.model';

@Component({
  selector: 'jhi-font-family-detail',
  templateUrl: './font-family-detail.component.html',
})
export class FontFamilyDetailComponent implements OnInit {
  fontFamily: IFontFamily | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fontFamily }) => {
      this.fontFamily = fontFamily;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
