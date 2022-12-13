import { IStylesDetails, NewStylesDetails } from './styles-details.model';

export const sampleWithRequiredData: IStylesDetails = {
  id: 61082,
};

export const sampleWithPartialData: IStylesDetails = {
  id: 89136,
};

export const sampleWithFullData: IStylesDetails = {
  id: 3750,
  isActive: false,
  templateValue: 'Borders',
  replaceValue: 'killer impactful Tasty',
};

export const sampleWithNewData: NewStylesDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
