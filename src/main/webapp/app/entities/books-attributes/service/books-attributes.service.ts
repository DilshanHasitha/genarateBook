import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooksAttributes, NewBooksAttributes } from '../books-attributes.model';

export type PartialUpdateBooksAttributes = Partial<IBooksAttributes> & Pick<IBooksAttributes, 'id'>;

export type EntityResponseType = HttpResponse<IBooksAttributes>;
export type EntityArrayResponseType = HttpResponse<IBooksAttributes[]>;

@Injectable({ providedIn: 'root' })
export class BooksAttributesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books-attributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booksAttributes: NewBooksAttributes): Observable<EntityResponseType> {
    return this.http.post<IBooksAttributes>(this.resourceUrl, booksAttributes, { observe: 'response' });
  }

  update(booksAttributes: IBooksAttributes): Observable<EntityResponseType> {
    return this.http.put<IBooksAttributes>(`${this.resourceUrl}/${this.getBooksAttributesIdentifier(booksAttributes)}`, booksAttributes, {
      observe: 'response',
    });
  }

  partialUpdate(booksAttributes: PartialUpdateBooksAttributes): Observable<EntityResponseType> {
    return this.http.patch<IBooksAttributes>(`${this.resourceUrl}/${this.getBooksAttributesIdentifier(booksAttributes)}`, booksAttributes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooksAttributes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooksAttributes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksAttributesIdentifier(booksAttributes: Pick<IBooksAttributes, 'id'>): number {
    return booksAttributes.id;
  }

  compareBooksAttributes(o1: Pick<IBooksAttributes, 'id'> | null, o2: Pick<IBooksAttributes, 'id'> | null): boolean {
    return o1 && o2 ? this.getBooksAttributesIdentifier(o1) === this.getBooksAttributesIdentifier(o2) : o1 === o2;
  }

  addBooksAttributesToCollectionIfMissing<Type extends Pick<IBooksAttributes, 'id'>>(
    booksAttributesCollection: Type[],
    ...booksAttributesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const booksAttributes: Type[] = booksAttributesToCheck.filter(isPresent);
    if (booksAttributes.length > 0) {
      const booksAttributesCollectionIdentifiers = booksAttributesCollection.map(
        booksAttributesItem => this.getBooksAttributesIdentifier(booksAttributesItem)!
      );
      const booksAttributesToAdd = booksAttributes.filter(booksAttributesItem => {
        const booksAttributesIdentifier = this.getBooksAttributesIdentifier(booksAttributesItem);
        if (booksAttributesCollectionIdentifiers.includes(booksAttributesIdentifier)) {
          return false;
        }
        booksAttributesCollectionIdentifiers.push(booksAttributesIdentifier);
        return true;
      });
      return [...booksAttributesToAdd, ...booksAttributesCollection];
    }
    return booksAttributesCollection;
  }
}
