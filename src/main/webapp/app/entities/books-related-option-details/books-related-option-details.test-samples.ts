import { IBooksRelatedOptionDetails, NewBooksRelatedOptionDetails } from './books-related-option-details.model';

export const sampleWithRequiredData: IBooksRelatedOptionDetails = {
  id: 98430,
  description: 'Customizable magenta Zambia',
};

export const sampleWithPartialData: IBooksRelatedOptionDetails = {
  id: 41593,
  description: 'connecting compressing Austria',
};

export const sampleWithFullData: IBooksRelatedOptionDetails = {
  id: 22090,
  code: 'neural Forge',
  description: 'Soft',
  isActive: false,
};

export const sampleWithNewData: NewBooksRelatedOptionDetails = {
  description: 'copy payment',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
