import { IPriceRelatedOption } from 'app/entities/price-related-option/price-related-option.model';

export interface IPriceRelatedOptionDetails {
  id: number;
  description?: string | null;
  price?: number | null;
  priceRelatedOptions?: Pick<IPriceRelatedOption, 'id'>[] | null;
}

export type NewPriceRelatedOptionDetails = Omit<IPriceRelatedOptionDetails, 'id'> & { id: null };
