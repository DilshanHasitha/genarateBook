import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStyles } from '../styles.model';

@Component({
  selector: 'jhi-styles-detail',
  templateUrl: './styles-detail.component.html',
})
export class StylesDetailComponent implements OnInit {
  styles: IStyles | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ styles }) => {
      this.styles = styles;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
