import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFontFamily, NewFontFamily } from '../font-family.model';

export type PartialUpdateFontFamily = Partial<IFontFamily> & Pick<IFontFamily, 'id'>;

export type EntityResponseType = HttpResponse<IFontFamily>;
export type EntityArrayResponseType = HttpResponse<IFontFamily[]>;

@Injectable({ providedIn: 'root' })
export class FontFamilyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/font-families');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fontFamily: NewFontFamily): Observable<EntityResponseType> {
    return this.http.post<IFontFamily>(this.resourceUrl, fontFamily, { observe: 'response' });
  }

  update(fontFamily: IFontFamily): Observable<EntityResponseType> {
    return this.http.put<IFontFamily>(`${this.resourceUrl}/${this.getFontFamilyIdentifier(fontFamily)}`, fontFamily, {
      observe: 'response',
    });
  }

  partialUpdate(fontFamily: PartialUpdateFontFamily): Observable<EntityResponseType> {
    return this.http.patch<IFontFamily>(`${this.resourceUrl}/${this.getFontFamilyIdentifier(fontFamily)}`, fontFamily, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFontFamily>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFontFamily[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFontFamilyIdentifier(fontFamily: Pick<IFontFamily, 'id'>): number {
    return fontFamily.id;
  }

  compareFontFamily(o1: Pick<IFontFamily, 'id'> | null, o2: Pick<IFontFamily, 'id'> | null): boolean {
    return o1 && o2 ? this.getFontFamilyIdentifier(o1) === this.getFontFamilyIdentifier(o2) : o1 === o2;
  }

  addFontFamilyToCollectionIfMissing<Type extends Pick<IFontFamily, 'id'>>(
    fontFamilyCollection: Type[],
    ...fontFamiliesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fontFamilies: Type[] = fontFamiliesToCheck.filter(isPresent);
    if (fontFamilies.length > 0) {
      const fontFamilyCollectionIdentifiers = fontFamilyCollection.map(fontFamilyItem => this.getFontFamilyIdentifier(fontFamilyItem)!);
      const fontFamiliesToAdd = fontFamilies.filter(fontFamilyItem => {
        const fontFamilyIdentifier = this.getFontFamilyIdentifier(fontFamilyItem);
        if (fontFamilyCollectionIdentifiers.includes(fontFamilyIdentifier)) {
          return false;
        }
        fontFamilyCollectionIdentifiers.push(fontFamilyIdentifier);
        return true;
      });
      return [...fontFamiliesToAdd, ...fontFamilyCollection];
    }
    return fontFamilyCollection;
  }
}
