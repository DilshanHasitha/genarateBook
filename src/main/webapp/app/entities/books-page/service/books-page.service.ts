import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooksPage, NewBooksPage } from '../books-page.model';

export type PartialUpdateBooksPage = Partial<IBooksPage> & Pick<IBooksPage, 'id'>;

export type EntityResponseType = HttpResponse<IBooksPage>;
export type EntityArrayResponseType = HttpResponse<IBooksPage[]>;

@Injectable({ providedIn: 'root' })
export class BooksPageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books-pages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booksPage: NewBooksPage): Observable<EntityResponseType> {
    return this.http.post<IBooksPage>(this.resourceUrl, booksPage, { observe: 'response' });
  }

  update(booksPage: IBooksPage): Observable<EntityResponseType> {
    return this.http.put<IBooksPage>(`${this.resourceUrl}/${this.getBooksPageIdentifier(booksPage)}`, booksPage, { observe: 'response' });
  }

  partialUpdate(booksPage: PartialUpdateBooksPage): Observable<EntityResponseType> {
    return this.http.patch<IBooksPage>(`${this.resourceUrl}/${this.getBooksPageIdentifier(booksPage)}`, booksPage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooksPage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooksPage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksPageIdentifier(booksPage: Pick<IBooksPage, 'id'>): number {
    return booksPage.id;
  }

  compareBooksPage(o1: Pick<IBooksPage, 'id'> | null, o2: Pick<IBooksPage, 'id'> | null): boolean {
    return o1 && o2 ? this.getBooksPageIdentifier(o1) === this.getBooksPageIdentifier(o2) : o1 === o2;
  }

  addBooksPageToCollectionIfMissing<Type extends Pick<IBooksPage, 'id'>>(
    booksPageCollection: Type[],
    ...booksPagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const booksPages: Type[] = booksPagesToCheck.filter(isPresent);
    if (booksPages.length > 0) {
      const booksPageCollectionIdentifiers = booksPageCollection.map(booksPageItem => this.getBooksPageIdentifier(booksPageItem)!);
      const booksPagesToAdd = booksPages.filter(booksPageItem => {
        const booksPageIdentifier = this.getBooksPageIdentifier(booksPageItem);
        if (booksPageCollectionIdentifiers.includes(booksPageIdentifier)) {
          return false;
        }
        booksPageCollectionIdentifiers.push(booksPageIdentifier);
        return true;
      });
      return [...booksPagesToAdd, ...booksPageCollection];
    }
    return booksPageCollection;
  }
}
