import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksPageFormService } from './books-page-form.service';
import { BooksPageService } from '../service/books-page.service';
import { IBooksPage } from '../books-page.model';
import { IPageLayers } from 'app/entities/page-layers/page-layers.model';
import { PageLayersService } from 'app/entities/page-layers/service/page-layers.service';

import { BooksPageUpdateComponent } from './books-page-update.component';

describe('BooksPage Management Update Component', () => {
  let comp: BooksPageUpdateComponent;
  let fixture: ComponentFixture<BooksPageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksPageFormService: BooksPageFormService;
  let booksPageService: BooksPageService;
  let pageLayersService: PageLayersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksPageUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BooksPageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksPageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksPageFormService = TestBed.inject(BooksPageFormService);
    booksPageService = TestBed.inject(BooksPageService);
    pageLayersService = TestBed.inject(PageLayersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PageLayers query and add missing value', () => {
      const booksPage: IBooksPage = { id: 456 };
      const pageDetails: IPageLayers[] = [{ id: 85922 }];
      booksPage.pageDetails = pageDetails;

      const pageLayersCollection: IPageLayers[] = [{ id: 53128 }];
      jest.spyOn(pageLayersService, 'query').mockReturnValue(of(new HttpResponse({ body: pageLayersCollection })));
      const additionalPageLayers = [...pageDetails];
      const expectedCollection: IPageLayers[] = [...additionalPageLayers, ...pageLayersCollection];
      jest.spyOn(pageLayersService, 'addPageLayersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ booksPage });
      comp.ngOnInit();

      expect(pageLayersService.query).toHaveBeenCalled();
      expect(pageLayersService.addPageLayersToCollectionIfMissing).toHaveBeenCalledWith(
        pageLayersCollection,
        ...additionalPageLayers.map(expect.objectContaining)
      );
      expect(comp.pageLayersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const booksPage: IBooksPage = { id: 456 };
      const pageDetails: IPageLayers = { id: 19592 };
      booksPage.pageDetails = [pageDetails];

      activatedRoute.data = of({ booksPage });
      comp.ngOnInit();

      expect(comp.pageLayersSharedCollection).toContain(pageDetails);
      expect(comp.booksPage).toEqual(booksPage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksPage>>();
      const booksPage = { id: 123 };
      jest.spyOn(booksPageFormService, 'getBooksPage').mockReturnValue(booksPage);
      jest.spyOn(booksPageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksPage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksPage }));
      saveSubject.complete();

      // THEN
      expect(booksPageFormService.getBooksPage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksPageService.update).toHaveBeenCalledWith(expect.objectContaining(booksPage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksPage>>();
      const booksPage = { id: 123 };
      jest.spyOn(booksPageFormService, 'getBooksPage').mockReturnValue({ id: null });
      jest.spyOn(booksPageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksPage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksPage }));
      saveSubject.complete();

      // THEN
      expect(booksPageFormService.getBooksPage).toHaveBeenCalled();
      expect(booksPageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksPage>>();
      const booksPage = { id: 123 };
      jest.spyOn(booksPageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksPage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksPageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePageLayers', () => {
      it('Should forward to pageLayersService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pageLayersService, 'comparePageLayers');
        comp.comparePageLayers(entity, entity2);
        expect(pageLayersService.comparePageLayers).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
