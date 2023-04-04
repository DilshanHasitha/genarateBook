import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrgPropertyDetailComponent } from './org-property-detail.component';

describe('OrgProperty Management Detail Component', () => {
  let comp: OrgPropertyDetailComponent;
  let fixture: ComponentFixture<OrgPropertyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrgPropertyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orgProperty: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrgPropertyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrgPropertyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orgProperty on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orgProperty).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
