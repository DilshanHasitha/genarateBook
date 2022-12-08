import { IAvatarCharactor, NewAvatarCharactor } from './avatar-charactor.model';

export const sampleWithRequiredData: IAvatarCharactor = {
  id: 29314,
  description: 'green integrated Legacy',
};

export const sampleWithPartialData: IAvatarCharactor = {
  id: 77383,
  code: 'Engineer Mountain Strategist',
  description: 'microchip',
  isActive: true,
};

export const sampleWithFullData: IAvatarCharactor = {
  id: 21790,
  code: 'sticky blue',
  description: 'parse systemic',
  isActive: false,
};

export const sampleWithNewData: NewAvatarCharactor = {
  description: 'Practical user-centric',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
