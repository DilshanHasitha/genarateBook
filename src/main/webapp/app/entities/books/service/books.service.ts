import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooks, NewBooks } from '../books.model';

export type PartialUpdateBooks = Partial<IBooks> & Pick<IBooks, 'id'>;

export type EntityResponseType = HttpResponse<IBooks>;
export type EntityArrayResponseType = HttpResponse<IBooks[]>;

@Injectable({ providedIn: 'root' })
export class BooksService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(books: NewBooks): Observable<EntityResponseType> {
    return this.http.post<IBooks>(this.resourceUrl, books, { observe: 'response' });
  }

  update(books: IBooks): Observable<EntityResponseType> {
    return this.http.put<IBooks>(`${this.resourceUrl}/${this.getBooksIdentifier(books)}`, books, { observe: 'response' });
  }

  partialUpdate(books: PartialUpdateBooks): Observable<EntityResponseType> {
    return this.http.patch<IBooks>(`${this.resourceUrl}/${this.getBooksIdentifier(books)}`, books, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooks>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooks[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksIdentifier(books: Pick<IBooks, 'id'>): number {
    return books.id;
  }

  compareBooks(o1: Pick<IBooks, 'id'> | null, o2: Pick<IBooks, 'id'> | null): boolean {
    return o1 && o2 ? this.getBooksIdentifier(o1) === this.getBooksIdentifier(o2) : o1 === o2;
  }

  addBooksToCollectionIfMissing<Type extends Pick<IBooks, 'id'>>(
    booksCollection: Type[],
    ...booksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const books: Type[] = booksToCheck.filter(isPresent);
    if (books.length > 0) {
      const booksCollectionIdentifiers = booksCollection.map(booksItem => this.getBooksIdentifier(booksItem)!);
      const booksToAdd = books.filter(booksItem => {
        const booksIdentifier = this.getBooksIdentifier(booksItem);
        if (booksCollectionIdentifiers.includes(booksIdentifier)) {
          return false;
        }
        booksCollectionIdentifiers.push(booksIdentifier);
        return true;
      });
      return [...booksToAdd, ...booksCollection];
    }
    return booksCollection;
  }
}
