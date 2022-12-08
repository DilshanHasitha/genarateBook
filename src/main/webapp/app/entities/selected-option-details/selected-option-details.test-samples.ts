import { ISelectedOptionDetails, NewSelectedOptionDetails } from './selected-option-details.model';

export const sampleWithRequiredData: ISelectedOptionDetails = {
  id: 93435,
};

export const sampleWithPartialData: ISelectedOptionDetails = {
  id: 40958,
  code: 'withdrawal Specialist Developer',
  name: 'National Agent',
  isActive: true,
};

export const sampleWithFullData: ISelectedOptionDetails = {
  id: 76978,
  code: 'blue high-level',
  name: 'Myanmar Costa transmit',
  selectedValue: 'hardware intangible',
  isActive: true,
};

export const sampleWithNewData: NewSelectedOptionDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
