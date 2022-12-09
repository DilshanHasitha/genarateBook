import { IAvatarAttributes, NewAvatarAttributes } from './avatar-attributes.model';

export const sampleWithRequiredData: IAvatarAttributes = {
  id: 84205,
  description: 'GB Forward',
};

export const sampleWithPartialData: IAvatarAttributes = {
  id: 28180,
  code: 'Web web-readiness engage',
  description: 'Bedfordshire alliance Cotton',
  isActive: true,
};

export const sampleWithFullData: IAvatarAttributes = {
  id: 11048,
  code: 'convergence',
  description: 'asymmetric',
  isActive: false,
  avatarAttributesCode: 'microchip solutions group',
};

export const sampleWithNewData: NewAvatarAttributes = {
  description: 'magnetic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
