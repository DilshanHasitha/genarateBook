import { IAvatarCharactor, NewAvatarCharactor } from './avatar-charactor.model';

export const sampleWithRequiredData: IAvatarCharactor = {
  id: 29314,
  description: 'green integrated Legacy',
};

export const sampleWithPartialData: IAvatarCharactor = {
  id: 46921,
  code: 'XSS Birr',
  description: 'mobile',
  isActive: true,
  imgUrl: 'purple Cheese',
  width: 73312,
  height: 27566,
  y: 86722,
};

export const sampleWithFullData: IAvatarCharactor = {
  id: 62723,
  code: 'evolve array',
  description: 'Handmade Small Israel',
  isActive: false,
  imgUrl: 'disintermediate',
  width: 68980,
  height: 29154,
  x: 46822,
  y: 47985,
};

export const sampleWithNewData: NewAvatarCharactor = {
  description: 'white Savings',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
