import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksFormService } from './books-form.service';
import { BooksService } from '../service/books.service';
import { IBooks } from '../books.model';
import { IPageSize } from 'app/entities/page-size/page-size.model';
import { PageSizeService } from 'app/entities/page-size/service/page-size.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBooksPage } from 'app/entities/books-page/books-page.model';
import { BooksPageService } from 'app/entities/books-page/service/books-page.service';
import { IPriceRelatedOption } from 'app/entities/price-related-option/price-related-option.model';
import { PriceRelatedOptionService } from 'app/entities/price-related-option/service/price-related-option.service';
import { IBooksRelatedOption } from 'app/entities/books-related-option/books-related-option.model';
import { BooksRelatedOptionService } from 'app/entities/books-related-option/service/books-related-option.service';
import { IBooksAttributes } from 'app/entities/books-attributes/books-attributes.model';
import { BooksAttributesService } from 'app/entities/books-attributes/service/books-attributes.service';
import { IBooksVariables } from 'app/entities/books-variables/books-variables.model';
import { BooksVariablesService } from 'app/entities/books-variables/service/books-variables.service';
import { IAvatarAttributes } from 'app/entities/avatar-attributes/avatar-attributes.model';
import { AvatarAttributesService } from 'app/entities/avatar-attributes/service/avatar-attributes.service';
import { ILayerGroup } from 'app/entities/layer-group/layer-group.model';
import { LayerGroupService } from 'app/entities/layer-group/service/layer-group.service';
import { ISelections } from 'app/entities/selections/selections.model';
import { SelectionsService } from 'app/entities/selections/service/selections.service';

import { BooksUpdateComponent } from './books-update.component';

describe('Books Management Update Component', () => {
  let comp: BooksUpdateComponent;
  let fixture: ComponentFixture<BooksUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksFormService: BooksFormService;
  let booksService: BooksService;
  let pageSizeService: PageSizeService;
  let userService: UserService;
  let booksPageService: BooksPageService;
  let priceRelatedOptionService: PriceRelatedOptionService;
  let booksRelatedOptionService: BooksRelatedOptionService;
  let booksAttributesService: BooksAttributesService;
  let booksVariablesService: BooksVariablesService;
  let avatarAttributesService: AvatarAttributesService;
  let layerGroupService: LayerGroupService;
  let selectionsService: SelectionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksUpdateComponent],
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
      .overrideTemplate(BooksUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksFormService = TestBed.inject(BooksFormService);
    booksService = TestBed.inject(BooksService);
    pageSizeService = TestBed.inject(PageSizeService);
    userService = TestBed.inject(UserService);
    booksPageService = TestBed.inject(BooksPageService);
    priceRelatedOptionService = TestBed.inject(PriceRelatedOptionService);
    booksRelatedOptionService = TestBed.inject(BooksRelatedOptionService);
    booksAttributesService = TestBed.inject(BooksAttributesService);
    booksVariablesService = TestBed.inject(BooksVariablesService);
    avatarAttributesService = TestBed.inject(AvatarAttributesService);
    layerGroupService = TestBed.inject(LayerGroupService);
    selectionsService = TestBed.inject(SelectionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PageSize query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const pageSize: IPageSize = { id: 84674 };
      books.pageSize = pageSize;

      const pageSizeCollection: IPageSize[] = [{ id: 5444 }];
      jest.spyOn(pageSizeService, 'query').mockReturnValue(of(new HttpResponse({ body: pageSizeCollection })));
      const additionalPageSizes = [pageSize];
      const expectedCollection: IPageSize[] = [...additionalPageSizes, ...pageSizeCollection];
      jest.spyOn(pageSizeService, 'addPageSizeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(pageSizeService.query).toHaveBeenCalled();
      expect(pageSizeService.addPageSizeToCollectionIfMissing).toHaveBeenCalledWith(
        pageSizeCollection,
        ...additionalPageSizes.map(expect.objectContaining)
      );
      expect(comp.pageSizesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const user: IUser = { id: 49384 };
      books.user = user;

      const userCollection: IUser[] = [{ id: 40056 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BooksPage query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const booksPages: IBooksPage[] = [{ id: 78365 }];
      books.booksPages = booksPages;

      const booksPageCollection: IBooksPage[] = [{ id: 55177 }];
      jest.spyOn(booksPageService, 'query').mockReturnValue(of(new HttpResponse({ body: booksPageCollection })));
      const additionalBooksPages = [...booksPages];
      const expectedCollection: IBooksPage[] = [...additionalBooksPages, ...booksPageCollection];
      jest.spyOn(booksPageService, 'addBooksPageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(booksPageService.query).toHaveBeenCalled();
      expect(booksPageService.addBooksPageToCollectionIfMissing).toHaveBeenCalledWith(
        booksPageCollection,
        ...additionalBooksPages.map(expect.objectContaining)
      );
      expect(comp.booksPagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PriceRelatedOption query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const priceRelatedOptions: IPriceRelatedOption[] = [{ id: 79285 }];
      books.priceRelatedOptions = priceRelatedOptions;

      const priceRelatedOptionCollection: IPriceRelatedOption[] = [{ id: 63891 }];
      jest.spyOn(priceRelatedOptionService, 'query').mockReturnValue(of(new HttpResponse({ body: priceRelatedOptionCollection })));
      const additionalPriceRelatedOptions = [...priceRelatedOptions];
      const expectedCollection: IPriceRelatedOption[] = [...additionalPriceRelatedOptions, ...priceRelatedOptionCollection];
      jest.spyOn(priceRelatedOptionService, 'addPriceRelatedOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(priceRelatedOptionService.query).toHaveBeenCalled();
      expect(priceRelatedOptionService.addPriceRelatedOptionToCollectionIfMissing).toHaveBeenCalledWith(
        priceRelatedOptionCollection,
        ...additionalPriceRelatedOptions.map(expect.objectContaining)
      );
      expect(comp.priceRelatedOptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BooksRelatedOption query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const booksRelatedOptions: IBooksRelatedOption[] = [{ id: 88468 }];
      books.booksRelatedOptions = booksRelatedOptions;

      const booksRelatedOptionCollection: IBooksRelatedOption[] = [{ id: 99948 }];
      jest.spyOn(booksRelatedOptionService, 'query').mockReturnValue(of(new HttpResponse({ body: booksRelatedOptionCollection })));
      const additionalBooksRelatedOptions = [...booksRelatedOptions];
      const expectedCollection: IBooksRelatedOption[] = [...additionalBooksRelatedOptions, ...booksRelatedOptionCollection];
      jest.spyOn(booksRelatedOptionService, 'addBooksRelatedOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(booksRelatedOptionService.query).toHaveBeenCalled();
      expect(booksRelatedOptionService.addBooksRelatedOptionToCollectionIfMissing).toHaveBeenCalledWith(
        booksRelatedOptionCollection,
        ...additionalBooksRelatedOptions.map(expect.objectContaining)
      );
      expect(comp.booksRelatedOptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BooksAttributes query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const booksAttributes: IBooksAttributes[] = [{ id: 64170 }];
      books.booksAttributes = booksAttributes;

      const booksAttributesCollection: IBooksAttributes[] = [{ id: 33442 }];
      jest.spyOn(booksAttributesService, 'query').mockReturnValue(of(new HttpResponse({ body: booksAttributesCollection })));
      const additionalBooksAttributes = [...booksAttributes];
      const expectedCollection: IBooksAttributes[] = [...additionalBooksAttributes, ...booksAttributesCollection];
      jest.spyOn(booksAttributesService, 'addBooksAttributesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(booksAttributesService.query).toHaveBeenCalled();
      expect(booksAttributesService.addBooksAttributesToCollectionIfMissing).toHaveBeenCalledWith(
        booksAttributesCollection,
        ...additionalBooksAttributes.map(expect.objectContaining)
      );
      expect(comp.booksAttributesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BooksVariables query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const booksVariables: IBooksVariables[] = [{ id: 17517 }];
      books.booksVariables = booksVariables;

      const booksVariablesCollection: IBooksVariables[] = [{ id: 682 }];
      jest.spyOn(booksVariablesService, 'query').mockReturnValue(of(new HttpResponse({ body: booksVariablesCollection })));
      const additionalBooksVariables = [...booksVariables];
      const expectedCollection: IBooksVariables[] = [...additionalBooksVariables, ...booksVariablesCollection];
      jest.spyOn(booksVariablesService, 'addBooksVariablesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(booksVariablesService.query).toHaveBeenCalled();
      expect(booksVariablesService.addBooksVariablesToCollectionIfMissing).toHaveBeenCalledWith(
        booksVariablesCollection,
        ...additionalBooksVariables.map(expect.objectContaining)
      );
      expect(comp.booksVariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AvatarAttributes query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const avatarAttributes: IAvatarAttributes[] = [{ id: 4511 }];
      books.avatarAttributes = avatarAttributes;

      const avatarAttributesCollection: IAvatarAttributes[] = [{ id: 97014 }];
      jest.spyOn(avatarAttributesService, 'query').mockReturnValue(of(new HttpResponse({ body: avatarAttributesCollection })));
      const additionalAvatarAttributes = [...avatarAttributes];
      const expectedCollection: IAvatarAttributes[] = [...additionalAvatarAttributes, ...avatarAttributesCollection];
      jest.spyOn(avatarAttributesService, 'addAvatarAttributesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(avatarAttributesService.query).toHaveBeenCalled();
      expect(avatarAttributesService.addAvatarAttributesToCollectionIfMissing).toHaveBeenCalledWith(
        avatarAttributesCollection,
        ...additionalAvatarAttributes.map(expect.objectContaining)
      );
      expect(comp.avatarAttributesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LayerGroup query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const layerGroups: ILayerGroup[] = [{ id: 62085 }];
      books.layerGroups = layerGroups;

      const layerGroupCollection: ILayerGroup[] = [{ id: 8394 }];
      jest.spyOn(layerGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: layerGroupCollection })));
      const additionalLayerGroups = [...layerGroups];
      const expectedCollection: ILayerGroup[] = [...additionalLayerGroups, ...layerGroupCollection];
      jest.spyOn(layerGroupService, 'addLayerGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(layerGroupService.query).toHaveBeenCalled();
      expect(layerGroupService.addLayerGroupToCollectionIfMissing).toHaveBeenCalledWith(
        layerGroupCollection,
        ...additionalLayerGroups.map(expect.objectContaining)
      );
      expect(comp.layerGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Selections query and add missing value', () => {
      const books: IBooks = { id: 456 };
      const selections: ISelections[] = [{ id: 53967 }];
      books.selections = selections;

      const selectionsCollection: ISelections[] = [{ id: 88439 }];
      jest.spyOn(selectionsService, 'query').mockReturnValue(of(new HttpResponse({ body: selectionsCollection })));
      const additionalSelections = [...selections];
      const expectedCollection: ISelections[] = [...additionalSelections, ...selectionsCollection];
      jest.spyOn(selectionsService, 'addSelectionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(selectionsService.query).toHaveBeenCalled();
      expect(selectionsService.addSelectionsToCollectionIfMissing).toHaveBeenCalledWith(
        selectionsCollection,
        ...additionalSelections.map(expect.objectContaining)
      );
      expect(comp.selectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const books: IBooks = { id: 456 };
      const pageSize: IPageSize = { id: 547 };
      books.pageSize = pageSize;
      const user: IUser = { id: 7103 };
      books.user = user;
      const booksPage: IBooksPage = { id: 34873 };
      books.booksPages = [booksPage];
      const priceRelatedOption: IPriceRelatedOption = { id: 52394 };
      books.priceRelatedOptions = [priceRelatedOption];
      const booksRelatedOption: IBooksRelatedOption = { id: 98357 };
      books.booksRelatedOptions = [booksRelatedOption];
      const booksAttributes: IBooksAttributes = { id: 2439 };
      books.booksAttributes = [booksAttributes];
      const booksVariables: IBooksVariables = { id: 85875 };
      books.booksVariables = [booksVariables];
      const avatarAttributes: IAvatarAttributes = { id: 48444 };
      books.avatarAttributes = [avatarAttributes];
      const layerGroup: ILayerGroup = { id: 26721 };
      books.layerGroups = [layerGroup];
      const selections: ISelections = { id: 73120 };
      books.selections = [selections];

      activatedRoute.data = of({ books });
      comp.ngOnInit();

      expect(comp.pageSizesSharedCollection).toContain(pageSize);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.booksPagesSharedCollection).toContain(booksPage);
      expect(comp.priceRelatedOptionsSharedCollection).toContain(priceRelatedOption);
      expect(comp.booksRelatedOptionsSharedCollection).toContain(booksRelatedOption);
      expect(comp.booksAttributesSharedCollection).toContain(booksAttributes);
      expect(comp.booksVariablesSharedCollection).toContain(booksVariables);
      expect(comp.avatarAttributesSharedCollection).toContain(avatarAttributes);
      expect(comp.layerGroupsSharedCollection).toContain(layerGroup);
      expect(comp.selectionsSharedCollection).toContain(selections);
      expect(comp.books).toEqual(books);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooks>>();
      const books = { id: 123 };
      jest.spyOn(booksFormService, 'getBooks').mockReturnValue(books);
      jest.spyOn(booksService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ books });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: books }));
      saveSubject.complete();

      // THEN
      expect(booksFormService.getBooks).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksService.update).toHaveBeenCalledWith(expect.objectContaining(books));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooks>>();
      const books = { id: 123 };
      jest.spyOn(booksFormService, 'getBooks').mockReturnValue({ id: null });
      jest.spyOn(booksService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ books: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: books }));
      saveSubject.complete();

      // THEN
      expect(booksFormService.getBooks).toHaveBeenCalled();
      expect(booksService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooks>>();
      const books = { id: 123 };
      jest.spyOn(booksService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ books });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePageSize', () => {
      it('Should forward to pageSizeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pageSizeService, 'comparePageSize');
        comp.comparePageSize(entity, entity2);
        expect(pageSizeService.comparePageSize).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBooksPage', () => {
      it('Should forward to booksPageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksPageService, 'compareBooksPage');
        comp.compareBooksPage(entity, entity2);
        expect(booksPageService.compareBooksPage).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePriceRelatedOption', () => {
      it('Should forward to priceRelatedOptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(priceRelatedOptionService, 'comparePriceRelatedOption');
        comp.comparePriceRelatedOption(entity, entity2);
        expect(priceRelatedOptionService.comparePriceRelatedOption).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBooksRelatedOption', () => {
      it('Should forward to booksRelatedOptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksRelatedOptionService, 'compareBooksRelatedOption');
        comp.compareBooksRelatedOption(entity, entity2);
        expect(booksRelatedOptionService.compareBooksRelatedOption).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBooksAttributes', () => {
      it('Should forward to booksAttributesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksAttributesService, 'compareBooksAttributes');
        comp.compareBooksAttributes(entity, entity2);
        expect(booksAttributesService.compareBooksAttributes).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBooksVariables', () => {
      it('Should forward to booksVariablesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksVariablesService, 'compareBooksVariables');
        comp.compareBooksVariables(entity, entity2);
        expect(booksVariablesService.compareBooksVariables).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAvatarAttributes', () => {
      it('Should forward to avatarAttributesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(avatarAttributesService, 'compareAvatarAttributes');
        comp.compareAvatarAttributes(entity, entity2);
        expect(avatarAttributesService.compareAvatarAttributes).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLayerGroup', () => {
      it('Should forward to layerGroupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(layerGroupService, 'compareLayerGroup');
        comp.compareLayerGroup(entity, entity2);
        expect(layerGroupService.compareLayerGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSelections', () => {
      it('Should forward to selectionsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(selectionsService, 'compareSelections');
        comp.compareSelections(entity, entity2);
        expect(selectionsService.compareSelections).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
