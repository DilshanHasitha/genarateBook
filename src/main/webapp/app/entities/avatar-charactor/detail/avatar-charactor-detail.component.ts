import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAvatarCharactor } from '../avatar-charactor.model';

@Component({
  selector: 'jhi-avatar-charactor-detail',
  templateUrl: './avatar-charactor-detail.component.html',
})
export class AvatarCharactorDetailComponent implements OnInit {
  avatarCharactor: IAvatarCharactor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avatarCharactor }) => {
      this.avatarCharactor = avatarCharactor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
