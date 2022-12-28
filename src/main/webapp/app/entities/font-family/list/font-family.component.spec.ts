import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FontFamilyService } from '../service/font-family.service';

import { FontFamilyComponent } from './font-family.component';

describe('FontFamily Management Component', () => {
  let comp: FontFamilyComponent;
  let fixture: ComponentFixture<FontFamilyComponent>;
  let service: FontFamilyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'font-family', component: FontFamilyComponent }]), HttpClientTestingModule],
      declarations: [FontFamilyComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FontFamilyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FontFamilyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FontFamilyService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.fontFamilies?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to fontFamilyService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFontFamilyIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFontFamilyIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
