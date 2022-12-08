import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooksRelatedOptionDetails, NewBooksRelatedOptionDetails } from '../books-related-option-details.model';

export type PartialUpdateBooksRelatedOptionDetails = Partial<IBooksRelatedOptionDetails> & Pick<IBooksRelatedOptionDetails, 'id'>;

export type EntityResponseType = HttpResponse<IBooksRelatedOptionDetails>;
export type EntityArrayResponseType = HttpResponse<IBooksRelatedOptionDetails[]>;

@Injectable({ providedIn: 'root' })
export class BooksRelatedOptionDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books-related-option-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booksRelatedOptionDetails: NewBooksRelatedOptionDetails): Observable<EntityResponseType> {
    return this.http.post<IBooksRelatedOptionDetails>(this.resourceUrl, booksRelatedOptionDetails, { observe: 'response' });
  }

  update(booksRelatedOptionDetails: IBooksRelatedOptionDetails): Observable<EntityResponseType> {
    return this.http.put<IBooksRelatedOptionDetails>(
      `${this.resourceUrl}/${this.getBooksRelatedOptionDetailsIdentifier(booksRelatedOptionDetails)}`,
      booksRelatedOptionDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(booksRelatedOptionDetails: PartialUpdateBooksRelatedOptionDetails): Observable<EntityResponseType> {
    return this.http.patch<IBooksRelatedOptionDetails>(
      `${this.resourceUrl}/${this.getBooksRelatedOptionDetailsIdentifier(booksRelatedOptionDetails)}`,
      booksRelatedOptionDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooksRelatedOptionDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooksRelatedOptionDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksRelatedOptionDetailsIdentifier(booksRelatedOptionDetails: Pick<IBooksRelatedOptionDetails, 'id'>): number {
    return booksRelatedOptionDetails.id;
  }

  compareBooksRelatedOptionDetails(
    o1: Pick<IBooksRelatedOptionDetails, 'id'> | null,
    o2: Pick<IBooksRelatedOptionDetails, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getBooksRelatedOptionDetailsIdentifier(o1) === this.getBooksRelatedOptionDetailsIdentifier(o2) : o1 === o2;
  }

  addBooksRelatedOptionDetailsToCollectionIfMissing<Type extends Pick<IBooksRelatedOptionDetails, 'id'>>(
    booksRelatedOptionDetailsCollection: Type[],
    ...booksRelatedOptionDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const booksRelatedOptionDetails: Type[] = booksRelatedOptionDetailsToCheck.filter(isPresent);
    if (booksRelatedOptionDetails.length > 0) {
      const booksRelatedOptionDetailsCollectionIdentifiers = booksRelatedOptionDetailsCollection.map(
        booksRelatedOptionDetailsItem => this.getBooksRelatedOptionDetailsIdentifier(booksRelatedOptionDetailsItem)!
      );
      const booksRelatedOptionDetailsToAdd = booksRelatedOptionDetails.filter(booksRelatedOptionDetailsItem => {
        const booksRelatedOptionDetailsIdentifier = this.getBooksRelatedOptionDetailsIdentifier(booksRelatedOptionDetailsItem);
        if (booksRelatedOptionDetailsCollectionIdentifiers.includes(booksRelatedOptionDetailsIdentifier)) {
          return false;
        }
        booksRelatedOptionDetailsCollectionIdentifiers.push(booksRelatedOptionDetailsIdentifier);
        return true;
      });
      return [...booksRelatedOptionDetailsToAdd, ...booksRelatedOptionDetailsCollection];
    }
    return booksRelatedOptionDetailsCollection;
  }
}
