import { IPriceRelatedOptionDetails, NewPriceRelatedOptionDetails } from './price-related-option-details.model';

export const sampleWithRequiredData: IPriceRelatedOptionDetails = {
  id: 66368,
  description: 'Liaison Regional port',
  price: 84969,
};

export const sampleWithPartialData: IPriceRelatedOptionDetails = {
  id: 24492,
  description: 'Shoals Table SMTP',
  price: 16479,
};

export const sampleWithFullData: IPriceRelatedOptionDetails = {
  id: 5024,
  description: 'Portugal Paradigm bandwidth',
  price: 30944,
};

export const sampleWithNewData: NewPriceRelatedOptionDetails = {
  description: 'enable',
  price: 55533,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
