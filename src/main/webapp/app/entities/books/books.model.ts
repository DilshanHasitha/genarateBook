import { IPageSize } from 'app/entities/page-size/page-size.model';
import { IUser } from 'app/entities/user/user.model';
import { IBooksPage } from 'app/entities/books-page/books-page.model';
import { IPriceRelatedOption } from 'app/entities/price-related-option/price-related-option.model';
import { IBooksRelatedOption } from 'app/entities/books-related-option/books-related-option.model';
import { IBooksAttributes } from 'app/entities/books-attributes/books-attributes.model';
import { IBooksVariables } from 'app/entities/books-variables/books-variables.model';
import { IAvatarAttributes } from 'app/entities/avatar-attributes/avatar-attributes.model';
import { ILayerGroup } from 'app/entities/layer-group/layer-group.model';
import { ISelections } from 'app/entities/selections/selections.model';

export interface IBooks {
  id: number;
  code?: string | null;
  name?: string | null;
  title?: string | null;
  subTitle?: string | null;
  author?: string | null;
  isActive?: boolean | null;
  noOfPages?: number | null;
  storeImg?: string | null;
  pageSize?: Pick<IPageSize, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
  booksPages?: Pick<IBooksPage, 'id'>[] | null;
  priceRelatedOptions?: Pick<IPriceRelatedOption, 'id'>[] | null;
  booksRelatedOptions?: Pick<IBooksRelatedOption, 'id'>[] | null;
  booksAttributes?: Pick<IBooksAttributes, 'id'>[] | null;
  booksVariables?: Pick<IBooksVariables, 'id'>[] | null;
  avatarAttributes?: Pick<IAvatarAttributes, 'id'>[] | null;
  layerGroups?: Pick<ILayerGroup, 'id'>[] | null;
  selections?: Pick<ISelections, 'id'>[] | null;
}

export type NewBooks = Omit<IBooks, 'id'> & { id: null };
