import { ISelectedOptionDetails, NewSelectedOptionDetails } from './selected-option-details.model';

export const sampleWithRequiredData: ISelectedOptionDetails = {
  id: 93435,
};

export const sampleWithPartialData: ISelectedOptionDetails = {
  id: 64602,
  code: 'website toolset',
  name: 'Underpass Zimbabwe',
  isActive: true,
  selectedOptionCode: 'index Fantastic River',
};

export const sampleWithFullData: ISelectedOptionDetails = {
  id: 54339,
  code: 'connecting Right-sized',
  name: 'e-tailers',
  selectedValue: 'intangible',
  isActive: true,
  selectedStyleCode: 'primary',
  selectedOptionCode: 'Designer Account quantify',
};

export const sampleWithNewData: NewSelectedOptionDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
