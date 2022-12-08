import { IBooksOptionDetails, NewBooksOptionDetails } from './books-option-details.model';

export const sampleWithRequiredData: IBooksOptionDetails = {
  id: 36188,
};

export const sampleWithPartialData: IBooksOptionDetails = {
  id: 12899,
  style: 'Granite portal',
  isActive: false,
};

export const sampleWithFullData: IBooksOptionDetails = {
  id: 85720,
  avatarAttributes: 'Circles Refined',
  avatarCharactor: 'technologies hard',
  style: 'Tasty primary Fantastic',
  option: 'Incredible',
  isActive: false,
};

export const sampleWithNewData: NewBooksOptionDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
