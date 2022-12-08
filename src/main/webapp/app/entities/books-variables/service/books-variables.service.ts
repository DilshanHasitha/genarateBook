import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooksVariables, NewBooksVariables } from '../books-variables.model';

export type PartialUpdateBooksVariables = Partial<IBooksVariables> & Pick<IBooksVariables, 'id'>;

export type EntityResponseType = HttpResponse<IBooksVariables>;
export type EntityArrayResponseType = HttpResponse<IBooksVariables[]>;

@Injectable({ providedIn: 'root' })
export class BooksVariablesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/books-variables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booksVariables: NewBooksVariables): Observable<EntityResponseType> {
    return this.http.post<IBooksVariables>(this.resourceUrl, booksVariables, { observe: 'response' });
  }

  update(booksVariables: IBooksVariables): Observable<EntityResponseType> {
    return this.http.put<IBooksVariables>(`${this.resourceUrl}/${this.getBooksVariablesIdentifier(booksVariables)}`, booksVariables, {
      observe: 'response',
    });
  }

  partialUpdate(booksVariables: PartialUpdateBooksVariables): Observable<EntityResponseType> {
    return this.http.patch<IBooksVariables>(`${this.resourceUrl}/${this.getBooksVariablesIdentifier(booksVariables)}`, booksVariables, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooksVariables>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooksVariables[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBooksVariablesIdentifier(booksVariables: Pick<IBooksVariables, 'id'>): number {
    return booksVariables.id;
  }

  compareBooksVariables(o1: Pick<IBooksVariables, 'id'> | null, o2: Pick<IBooksVariables, 'id'> | null): boolean {
    return o1 && o2 ? this.getBooksVariablesIdentifier(o1) === this.getBooksVariablesIdentifier(o2) : o1 === o2;
  }

  addBooksVariablesToCollectionIfMissing<Type extends Pick<IBooksVariables, 'id'>>(
    booksVariablesCollection: Type[],
    ...booksVariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const booksVariables: Type[] = booksVariablesToCheck.filter(isPresent);
    if (booksVariables.length > 0) {
      const booksVariablesCollectionIdentifiers = booksVariablesCollection.map(
        booksVariablesItem => this.getBooksVariablesIdentifier(booksVariablesItem)!
      );
      const booksVariablesToAdd = booksVariables.filter(booksVariablesItem => {
        const booksVariablesIdentifier = this.getBooksVariablesIdentifier(booksVariablesItem);
        if (booksVariablesCollectionIdentifiers.includes(booksVariablesIdentifier)) {
          return false;
        }
        booksVariablesCollectionIdentifiers.push(booksVariablesIdentifier);
        return true;
      });
      return [...booksVariablesToAdd, ...booksVariablesCollection];
    }
    return booksVariablesCollection;
  }
}
