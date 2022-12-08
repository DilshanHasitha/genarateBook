import { IAvatarAttributes, NewAvatarAttributes } from './avatar-attributes.model';

export const sampleWithRequiredData: IAvatarAttributes = {
  id: 84205,
  description: 'GB Forward',
};

export const sampleWithPartialData: IAvatarAttributes = {
  id: 17865,
  code: 'parsing',
  description: 'web-readiness',
  isActive: false,
};

export const sampleWithFullData: IAvatarAttributes = {
  id: 58341,
  code: 'XML orange back-end',
  description: 'primary convergence definition',
  isActive: true,
};

export const sampleWithNewData: NewAvatarAttributes = {
  description: 'calculating',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
