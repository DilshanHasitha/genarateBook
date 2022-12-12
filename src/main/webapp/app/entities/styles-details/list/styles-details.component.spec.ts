import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StylesDetailsService } from '../service/styles-details.service';

import { StylesDetailsComponent } from './styles-details.component';

describe('StylesDetails Management Component', () => {
  let comp: StylesDetailsComponent;
  let fixture: ComponentFixture<StylesDetailsComponent>;
  let service: StylesDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'styles-details', component: StylesDetailsComponent }]), HttpClientTestingModule],
      declarations: [StylesDetailsComponent],
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
      .overrideTemplate(StylesDetailsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StylesDetailsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StylesDetailsService);

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
    expect(comp.stylesDetails?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to stylesDetailsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getStylesDetailsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getStylesDetailsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
