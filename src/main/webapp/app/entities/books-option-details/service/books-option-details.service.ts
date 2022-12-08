import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooksOptionDetails, NewBooksOptionDetails } from '../books-option-details.model';

export type PartialUpdateBooksOptionDetails = Partial<IBooksOptionDetails> & Pick<IBooksOptionDetails, 'id'>;

export type EntityResponseType = HttpResponse<IBooksOptionDetails>;
export type EntityArrayResponseType = HttpResponse<IBooksOptionDetails[]>;

@Injectable({ providedIn: 'root' })
export class BooksOptionDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books-option-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booksOptionDetails: NewBooksOptionDetails): Observable<EntityResponseType> {
    return this.http.post<IBooksOptionDetails>(this.resourceUrl, booksOptionDetails, { observe: 'response' });
  }

  update(booksOptionDetails: IBooksOptionDetails): Observable<EntityResponseType> {
    return this.http.put<IBooksOptionDetails>(
      `${this.resourceUrl}/${this.getBooksOptionDetailsIdentifier(booksOptionDetails)}`,
      booksOptionDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(booksOptionDetails: PartialUpdateBooksOptionDetails): Observable<EntityResponseType> {
    return this.http.patch<IBooksOptionDetails>(
      `${this.resourceUrl}/${this.getBooksOptionDetailsIdentifier(booksOptionDetails)}`,
      booksOptionDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooksOptionDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooksOptionDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksOptionDetailsIdentifier(booksOptionDetails: Pick<IBooksOptionDetails, 'id'>): number {
    return booksOptionDetails.id;
  }

  compareBooksOptionDetails(o1: Pick<IBooksOptionDetails, 'id'> | null, o2: Pick<IBooksOptionDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getBooksOptionDetailsIdentifier(o1) === this.getBooksOptionDetailsIdentifier(o2) : o1 === o2;
  }

  addBooksOptionDetailsToCollectionIfMissing<Type extends Pick<IBooksOptionDetails, 'id'>>(
    booksOptionDetailsCollection: Type[],
    ...booksOptionDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const booksOptionDetails: Type[] = booksOptionDetailsToCheck.filter(isPresent);
    if (booksOptionDetails.length > 0) {
      const booksOptionDetailsCollectionIdentifiers = booksOptionDetailsCollection.map(
        booksOptionDetailsItem => this.getBooksOptionDetailsIdentifier(booksOptionDetailsItem)!
      );
      const booksOptionDetailsToAdd = booksOptionDetails.filter(booksOptionDetailsItem => {
        const booksOptionDetailsIdentifier = this.getBooksOptionDetailsIdentifier(booksOptionDetailsItem);
        if (booksOptionDetailsCollectionIdentifiers.includes(booksOptionDetailsIdentifier)) {
          return false;
        }
        booksOptionDetailsCollectionIdentifiers.push(booksOptionDetailsIdentifier);
        return true;
      });
      return [...booksOptionDetailsToAdd, ...booksOptionDetailsCollection];
    }
    return booksOptionDetailsCollection;
  }
}
