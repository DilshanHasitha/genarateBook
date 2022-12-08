import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AvatarCharactorDetailComponent } from './avatar-charactor-detail.component';

describe('AvatarCharactor Management Detail Component', () => {
  let comp: AvatarCharactorDetailComponent;
  let fixture: ComponentFixture<AvatarCharactorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AvatarCharactorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ avatarCharactor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AvatarCharactorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AvatarCharactorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load avatarCharactor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.avatarCharactor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
