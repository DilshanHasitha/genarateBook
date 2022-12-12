import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CharacterDetailComponent } from './character-detail.component';

describe('Character Management Detail Component', () => {
  let comp: CharacterDetailComponent;
  let fixture: ComponentFixture<CharacterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CharacterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ character: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CharacterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CharacterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load character on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.character).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
