import { IOptionType, NewOptionType } from './option-type.model';

export const sampleWithRequiredData: IOptionType = {
  id: 57960,
  description: 'real-time Usability',
};

export const sampleWithPartialData: IOptionType = {
  id: 72738,
  code: 'Credit Security Chicken',
  description: 'Configuration',
};

export const sampleWithFullData: IOptionType = {
  id: 32049,
  code: 'Tennessee',
  description: 'Mount monitor Garden',
  isActive: false,
};

export const sampleWithNewData: NewOptionType = {
  description: 'Dalasi Auto',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
