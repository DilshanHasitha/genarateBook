import { IAvatarAttributes, NewAvatarAttributes } from './avatar-attributes.model';

export const sampleWithRequiredData: IAvatarAttributes = {
  id: 84205,
  description: 'GB Forward',
};

export const sampleWithPartialData: IAvatarAttributes = {
  id: 84658,
  code: 'Checking Soap XML',
  description: 'alliance',
  isActive: false,
};

export const sampleWithFullData: IAvatarAttributes = {
  id: 39687,
  code: 'primary convergence definition',
  description: 'service-desk indigo',
  isActive: false,
  avatarAttributesCode: 'group pink white',
  templateText: 'alarm real-time mint',
};

export const sampleWithNewData: NewAvatarAttributes = {
  description: 'Guarani XML leverage',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
