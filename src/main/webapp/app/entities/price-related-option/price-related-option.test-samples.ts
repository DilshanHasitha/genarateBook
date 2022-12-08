import { IPriceRelatedOption, NewPriceRelatedOption } from './price-related-option.model';

export const sampleWithRequiredData: IPriceRelatedOption = {
  id: 85247,
  name: 'stable Implementation Panama',
};

export const sampleWithPartialData: IPriceRelatedOption = {
  id: 96278,
  name: 'olive Sports Frozen',
  isActive: false,
};

export const sampleWithFullData: IPriceRelatedOption = {
  id: 46539,
  code: 'panel Automotive Awesome',
  name: 'integrated Home',
  isActive: false,
};

export const sampleWithNewData: NewPriceRelatedOption = {
  name: 'SMTP navigating',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
