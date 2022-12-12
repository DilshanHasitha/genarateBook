import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CharacterService } from '../service/character.service';

import { CharacterComponent } from './character.component';

describe('Character Management Component', () => {
  let comp: CharacterComponent;
  let fixture: ComponentFixture<CharacterComponent>;
  let service: CharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'character', component: CharacterComponent }]), HttpClientTestingModule],
      declarations: [CharacterComponent],
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
      .overrideTemplate(CharacterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CharacterService);

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
    expect(comp.characters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to characterService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCharacterIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCharacterIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
