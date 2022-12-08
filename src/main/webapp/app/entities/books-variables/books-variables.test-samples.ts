import { IBooksVariables, NewBooksVariables } from './books-variables.model';

export const sampleWithRequiredData: IBooksVariables = {
  id: 32159,
};

export const sampleWithPartialData: IBooksVariables = {
  id: 9601,
  description: 'web-enabled Automated',
  isActive: true,
};

export const sampleWithFullData: IBooksVariables = {
  id: 69801,
  code: 'Plains Bedfordshire',
  description: 'SQL',
  isActive: true,
};

export const sampleWithNewData: NewBooksVariables = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
