import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPageSize } from '../page-size.model';

@Component({
  selector: 'jhi-page-size-detail',
  templateUrl: './page-size-detail.component.html',
})
export class PageSizeDetailComponent implements OnInit {
  pageSize: IPageSize | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageSize }) => {
      this.pageSize = pageSize;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
