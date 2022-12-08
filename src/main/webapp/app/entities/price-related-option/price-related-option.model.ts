import { IOptionType } from 'app/entities/option-type/option-type.model';
import { IPriceRelatedOptionDetails } from 'app/entities/price-related-option-details/price-related-option-details.model';
import { IBooks } from 'app/entities/books/books.model';

export interface IPriceRelatedOption {
  id: number;
  code?: string | null;
  name?: string | null;
  isActive?: boolean | null;
  optionType?: Pick<IOptionType, 'id'> | null;
  priceRelatedOptionDetails?: Pick<IPriceRelatedOptionDetails, 'id'>[] | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewPriceRelatedOption = Omit<IPriceRelatedOption, 'id'> & { id: null };
