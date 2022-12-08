import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStyles, NewStyles } from '../styles.model';

export type PartialUpdateStyles = Partial<IStyles> & Pick<IStyles, 'id'>;

export type EntityResponseType = HttpResponse<IStyles>;
export type EntityArrayResponseType = HttpResponse<IStyles[]>;

@Injectable({ providedIn: 'root' })
export class StylesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/styles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(styles: NewStyles): Observable<EntityResponseType> {
    return this.http.post<IStyles>(this.resourceUrl, styles, { observe: 'response' });
  }

  update(styles: IStyles): Observable<EntityResponseType> {
    return this.http.put<IStyles>(`${this.resourceUrl}/${this.getStylesIdentifier(styles)}`, styles, { observe: 'response' });
  }

  partialUpdate(styles: PartialUpdateStyles): Observable<EntityResponseType> {
    return this.http.patch<IStyles>(`${this.resourceUrl}/${this.getStylesIdentifier(styles)}`, styles, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStyles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStyles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStylesIdentifier(styles: Pick<IStyles, 'id'>): number {
    return styles.id;
  }

  compareStyles(o1: Pick<IStyles, 'id'> | null, o2: Pick<IStyles, 'id'> | null): boolean {
    return o1 && o2 ? this.getStylesIdentifier(o1) === this.getStylesIdentifier(o2) : o1 === o2;
  }

  addStylesToCollectionIfMissing<Type extends Pick<IStyles, 'id'>>(
    stylesCollection: Type[],
    ...stylesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const styles: Type[] = stylesToCheck.filter(isPresent);
    if (styles.length > 0) {
      const stylesCollectionIdentifiers = stylesCollection.map(stylesItem => this.getStylesIdentifier(stylesItem)!);
      const stylesToAdd = styles.filter(stylesItem => {
        const stylesIdentifier = this.getStylesIdentifier(stylesItem);
        if (stylesCollectionIdentifiers.includes(stylesIdentifier)) {
          return false;
        }
        stylesCollectionIdentifiers.push(stylesIdentifier);
        return true;
      });
      return [...stylesToAdd, ...stylesCollection];
    }
    return stylesCollection;
  }
}
