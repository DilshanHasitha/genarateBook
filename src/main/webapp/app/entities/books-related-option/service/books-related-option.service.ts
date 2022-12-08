import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooksRelatedOption, NewBooksRelatedOption } from '../books-related-option.model';

export type PartialUpdateBooksRelatedOption = Partial<IBooksRelatedOption> & Pick<IBooksRelatedOption, 'id'>;

export type EntityResponseType = HttpResponse<IBooksRelatedOption>;
export type EntityArrayResponseType = HttpResponse<IBooksRelatedOption[]>;

@Injectable({ providedIn: 'root' })
export class BooksRelatedOptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books-related-options');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booksRelatedOption: NewBooksRelatedOption): Observable<EntityResponseType> {
    return this.http.post<IBooksRelatedOption>(this.resourceUrl, booksRelatedOption, { observe: 'response' });
  }

  update(booksRelatedOption: IBooksRelatedOption): Observable<EntityResponseType> {
    return this.http.put<IBooksRelatedOption>(
      `${this.resourceUrl}/${this.getBooksRelatedOptionIdentifier(booksRelatedOption)}`,
      booksRelatedOption,
      { observe: 'response' }
    );
  }

  partialUpdate(booksRelatedOption: PartialUpdateBooksRelatedOption): Observable<EntityResponseType> {
    return this.http.patch<IBooksRelatedOption>(
      `${this.resourceUrl}/${this.getBooksRelatedOptionIdentifier(booksRelatedOption)}`,
      booksRelatedOption,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooksRelatedOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooksRelatedOption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksRelatedOptionIdentifier(booksRelatedOption: Pick<IBooksRelatedOption, 'id'>): number {
    return booksRelatedOption.id;
  }

  compareBooksRelatedOption(o1: Pick<IBooksRelatedOption, 'id'> | null, o2: Pick<IBooksRelatedOption, 'id'> | null): boolean {
    return o1 && o2 ? this.getBooksRelatedOptionIdentifier(o1) === this.getBooksRelatedOptionIdentifier(o2) : o1 === o2;
  }

  addBooksRelatedOptionToCollectionIfMissing<Type extends Pick<IBooksRelatedOption, 'id'>>(
    booksRelatedOptionCollection: Type[],
    ...booksRelatedOptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const booksRelatedOptions: Type[] = booksRelatedOptionsToCheck.filter(isPresent);
    if (booksRelatedOptions.length > 0) {
      const booksRelatedOptionCollectionIdentifiers = booksRelatedOptionCollection.map(
        booksRelatedOptionItem => this.getBooksRelatedOptionIdentifier(booksRelatedOptionItem)!
      );
      const booksRelatedOptionsToAdd = booksRelatedOptions.filter(booksRelatedOptionItem => {
        const booksRelatedOptionIdentifier = this.getBooksRelatedOptionIdentifier(booksRelatedOptionItem);
        if (booksRelatedOptionCollectionIdentifiers.includes(booksRelatedOptionIdentifier)) {
          return false;
        }
        booksRelatedOptionCollectionIdentifiers.push(booksRelatedOptionIdentifier);
        return true;
      });
      return [...booksRelatedOptionsToAdd, ...booksRelatedOptionCollection];
    }
    return booksRelatedOptionCollection;
  }
}
